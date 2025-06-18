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

	ll p;
	cout<<"Nhap so nguyen to P: ", cin>>p;

	ll g;
	cout<<"Nhap phan tu sinh \"1 < so nguyen < p\": ", cin>>g;

	ll khoaBiMat = rand() % (p-2)+1;

	ll khoaCongKhai = Mu(g, khoaBiMat, p);

	ll banRo;
	cout<<"Nhap ban ro\"0 < so nguyen < p\": ", cin>>banRo;

	ll khoaNgauNhien = rand() % (p-2) + 1;
	ll banMa1 = Mu(g, khoaNgauNhien, p);
	ll banMa2 = (banRo * Mu(khoaCongKhai, khoaNgauNhien, p)) % p;
	
	cout<<"Ban ma la: ("<<banMa1<<", "<<banMa2<<")\n";

	ll giaiMa1 = Mu(banMa1, khoaBiMat, p);
	ll giaiMa2 = NghichDaoModulo(giaiMa1, p);
	ll maGiai = (banMa2 * giaiMa2) % p;

	cout<<"Ma da giai la: "<<maGiai<<'\n';

}