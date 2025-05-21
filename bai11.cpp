#include <bits/stdc++.h>
using namespace std;
#define ll long long

struct sinhVien {
	string maSV;
	string hoDem;
	string ten;
	string gioiTinh;
	int namSinh;
	float diemTK;	
};

struct Node {
	sinhVien inFor;
	Node *next;
};

typedef Node *tro;

void khoiTao(tro &L){
	L = NULL;
}

bool rong(tro L){
	return L==NULL;
}

bool nhapSV(sinhVien &x){
	cout<<"Nhap ma SV: ", cin>>x.maSV; cin.ignore();
	if(x.maSV=="**") return false;
	cout<<"Nhap Ho dem: ", getline(cin, x.hoDem);
	cout<<"Nhap ten: ", cin>>x.ten;
	cout<<"Nhap gioi tinh: ", cin>>x.gioiTinh;
	cout<<"Nhap nam sinh: ", cin>>x.namSinh;
	cout<<"Nhap diem TK: ", cin>>x.diemTK;
	return true;
}

void nhapDS(tro &L){
    khoiTao(L);
	sinhVien sv;
	tro Q;
	while (nhapSV(sv)){
		tro p = new Node;
		p->inFor = sv;
		p->next=NULL;
		if(rong(L))
			L=p;
		else
			Q->next = p;
		Q = p;	
	}
}

void xuat(tro L){
	tro Q=L;
	int dem=0;
	while(!rong(Q)){
		sinhVien sv = Q->inFor;
		Q = Q->next;
		dem++;
		cout<<dem<<": "<<sv.maSV<<"\n";
	}
}
int main() {
	tro danhSachSV;
	nhapDS(danhSachSV);
	xuat(danhSachSV);
}

/**

1
1
1
1
1
1
2
2
2
2
2
2
3
3
3
3
3
3
4
4
4
4
4
4


 */