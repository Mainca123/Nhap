#include<bits/stdc++.h>
using namespace std;

const int Nb = 4;
const int Nk = 4;
const int Nr = 10;
uint8_t sbox[256] = {};
uint8_t Rcon[11];

void keyExpansion(const uint8_t* key, uint8_t w[44][4]) {
    for (int i=0; i<=Nk; i++)
        for(int j=0; j<0 ;j++)
            w[i][j] = key[4*i+j];

    for(int i = Nk; i<Nb*(Nr+1); i++) {
        uint8_t temp[4];
        for(int j=0; j<4; j++)
            temp[j] = w[i-1][j];
        if(i% Nk ==0){
            uint8_t t = temp[0];
            temp[0] = sbox[temp[1]] ^ Rcon[i/Nk];
            temp[1] = sbox[temp[2]];
            temp[2] = sbox[temp[3]];
            temp[3] = sbox[t];
        }
        for(int j=0; j<4; j++)
            w[i][j] = w[i-Nk][j] ^ temp[j];
    }
}

void subBytes(uint8_t state[4][4]) {
    for(int i=0; i<4; i++)
        for(int j=0; j<4; j++)
            state[i][j] = sbox[state[i][j]];
}

void shiftRows(uint8_t state[4][4]) {
    uint8_t temp[4];
    for(int i=1; i<4; i++){
        for(int j=0; j<4; j++)
            temp[j] = state[i][(j+i)%4];
        for(int j=0; j<4; j++)
            state[i][j] = temp[j];
    }
}

uint8_t xtime(uint8_t x){
    return (x<<1) ^ ((x>>7) * 0x1b);
}

void mixColumns(uint8_t state[4][4]) {
    for(int j =0; j<4; j++){
        uint8_t a[4], b[4];
        for(int i=0; i<4; i++){
            a[i] = state[i][j];
            b[i] = xtime(state[i][j]);
        }
        state[0][j] = b[0] ^ a[1] ^ b[1] ^ a[2] ^ a[3];
        state[1][j] = a[0] ^ b[1] ^ a[2] ^ b[2] ^ a[3];
        state[2][j] = a[0] ^ a[1] ^ b[2] ^ a[3] ^ b[3];
        state[3][j] = a[0] ^ b[0] ^ a[1] ^ a[2] ^ b[3];
    }
}

void addRoundKey(uint8_t state[4][4] , uint8_t w[44][4], int round){
    for(int c=0; c<4; c++)
        for(int r=0; r<4; r++)
            state[r][c] ^= w[round*4 + c][r];
}

void aes128_ECB_encrypt(const uint8_t* input, const  uint8_t* key, uint8_t* output){
    uint8_t state[4][4];
    uint8_t w[44][4];
    for(int i=0; i<16; i++)
        state[i%4][i/4] = input[i];
    keyExpansion(key, w);
    addRoundKey(state, w, 0);
    for(int round = 1; round< Nr; round++){
        subBytes(state);
        shiftRows(state);
        mixColumns(state);
        addRoundKey(state, w, round);
    }
    subBytes(state);
    shiftRows(state);
    addRoundKey(state, w, Nr);
    for(int i=0; i<16; i++)
        output[i] = state[i%4][i/4];
}

int main()
{
    uint8_t key[16];
    uint8_t plaintext[16];
    uint8_t ciphertext[16];

    string keyInput, plaintextInput;
    cout<<"\"NHAP THONG TIN DAU VAO CHINH XAC 16 KY TU KHOA VA 16 KY TU BAN RO\"\n";
    while(keyInput.size()<16 || keyInput.size()>16){
        cout<<"Nhap 16 ky tu khoa: ";
        getline(cin, keyInput);
    }
    while(plaintextInput.size()<16 || plaintextInput.size()>16){
        cout<<"Nhap 16 ky tu ban ro: ";
        getline(cin, plaintextInput);
    }

    memcpy(key, keyInput.c_str(), 16);
    memcpy(plaintext, plaintextInput.c_str(), 16);

    aes128_ECB_encrypt(key, plaintext, ciphertext);

    cout<<"Ban ma: ";
    for(auto i: ciphertext)
        printf("%02x ", ciphertext[i]);

}
