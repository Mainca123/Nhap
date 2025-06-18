#include <bits/stdc++.h>
using namespace std;
#define ll long long

ll Mu(ll a, ll n, ll mod){
    ll res = 1;
    a = a % mod;
    while (n > 0){
        if (n % 2 == 1)
            res = (res * a) % mod;
        a = (a * a) % mod;
        n /= 2;
    }
    return res;
}

bool nt(ll a){
	if(a<2) return 0;
	for(int i=2; i*i<=a; i++)
		if(a%i==0) return 0;
	return 1;
}


ll NghichDaoModulo(ll e, ll phi){
	ll tCu=0, tMoi=1;
	ll rCu=phi, rMoi=e;
	while(rMoi!=0){
		ll tam = tMoi;
		tMoi = tCu - (rCu/rMoi)*tMoi;
		tCu=tam;

		tam=rMoi;
		rMoi = rCu%rMoi;
		rCu = tam; 
	}
	if(rCu == 0) return -1;
	if(tCu<0) tCu += phi;
	return tCu;

}

int main() {
	cout<<"Nhap so nguyen to P = ";
	ll p;
	cin>>p;
	while (!nt(p)){
		cout<<"Nhap lai P = ";
		cin>>p;
	}
	
	cout<<"Nhap so nguyen to Q = ";
	ll q;
	cin>>q;
	while (!nt(q)){
		cout<<"Nhap lai Q = ";
		cin>>q;
	}
	
	ll n=p*q;
	ll phi=(p-1)*(q-1);

	ll e;
	cout<<"Nhap khoa cong khai E = ", cin>>e;
	if(__gcd(e,phi) != 1)
		cout<<"Khoa cong khai khong hop le !!!\n";
	while( __gcd(e,phi) != 1){
		cout<<"Nhap lai E = ",cin>>e;
	}

	ll d=NghichDaoModulo(e,phi);

	ll banRo;
	cout<<"Nhap ban ro \"so nguyen < "<<n<<"\": ";
	cin>>banRo;
	while(banRo>=n) {
		cout<<"Nhap lai ban ro: ",cin>>banRo;
	}

	ll banMa = Mu(banRo, e, n);
	cout<<"\nBan ma la: "<<banMa<<"\n\n";

	cout<<"Ban ro sau khi giai ma la: "<<Mu(banMa, d, n)<<'\n';

}