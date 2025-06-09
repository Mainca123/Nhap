#include <bits/stdc++.h>
using namespace std;
#define ll long long

ll modInverse(ll e, ll phi){
    ll  t=0, newt=2,
        r=phi, newr=e;
    while(newr != 0){
        ll quotient=r / newr;
        ll temp=newt;
        newt=t - quotient * newt;
        t=temp;
        newr=r - quotient * newr;
        r=temp;
    }
    if(r>1) return -1;
    if(t<0) t+=phi;
    return t;
}

ll Pow(ll a, ll n, ll mod){
    if(n==0) return 1;
    ll tam=Pow(a, n/2, mod);
    tam*=tam%mod;
    tam%=mod;
    if(n%2 !=0)
        tam*=a%mod;
    tam%=mod;
    return tam;
}

bool nt(ll a){
    if(a<2) return 0;
    for(int i=2; i*i<=a; i++)
        if(a%i==0) return 0;
    return 1;
}

ll locNguyenTo(ll a){
    while(!nt(a))
        a--;
    return a;
}
int main()
{
    cout<<"Nhap gioi han tren cua 2 so nguyen to\n";
    ll a,b;
    cout<<"Nhap gioi han 1: ", cin>>a, a=locNguyenTo(a);
    cout<<"So nguyen to lon nhat la: "<<a<<'\n';
    cout<<"Nhap gioi han 2: ", cin>>b, b=locNguyenTo(b);
    cout<<"So nguyen to lon nhat la: "<<b<<'\n';
    ll n=a * b;
    ll phi=(a-1) * (b-1);
    cout<<"Ta co phi = "<<phi<<'\n';
    ll e;
    cout<<"Nhap 1 < e < phi: ", cin>>e;
    while(__gcd(e,phi) != 1)
        e++;

    ll d=modInverse(e, phi);
    if(d == -1){
        cout<<"Khong tim duoc d hop le";
        return 1;
    }

    cout<<"Public key: n = "<<n<<" , e = "<<e<<'\n';
    cout<<"Private key: n = "<<n<<" , d = "<<d<<'\n';
    ll banRo;
    cout<<"Nhap ban ro (so nguyen < "<<n<<"): ", cin>> banRo;

    ll banMa = Pow(banRo, e, n);
    cout<<"Ban ma: "<< banMa<<'\n';

    ll giaiMa = Pow(banMa, d, n);
    cout<<"Giai ma: "<< giaiMa;
}
