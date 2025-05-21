#include <bits/stdc++.h>
using namespace std;

const int MAX = 100;

struct HocVien
{
	char maHV[10];
	string tenHV;
	string gioiTinh;
	float diem;	
};

struct List
{
	int cout;
	HocVien a[MAX];
};

void Create(List &L){
	L.cout = -1;
}

bool Full (List L){
	return L.cout == MAX-1;
}

bool Empty(List L){
	return L.cout == -1;
}

bool Add(List &L, HocVien x) {
	if(Full(L)) return false;
	L.cout++;
	L.a[L.cout] = x;
	return true;
}

void Nhap(HocVien &hv){
	cout<<"Nhap ma hoc vien: ";
	fflush(stdin);
	gets(hv.maHV);
	cout<<"Nhap ten hoc vien: ";
	fflush(stdin);
	getline(cin, hv.tenHV);
	cout<<"Nhap gioi tinh hoc vien: ";
	fflush(stdin);
	cin>>hv.gioiTinh;
	cout<<"Nhap diem hoc vien: ", cin>>hv.diem;
}

void NhapDS(List &L) {
	Create(L);
	HocVien x;
	int i=0;
	cout<<"Nhap \'kk\' vao ma HV de dung nhap\n";
	cout<<"NHAP THONG TIN DANH SACH\n";
	do{
		i++;
		cout<<"Nhap thong tin cho hoc vien "<<i<<": \n";
		Nhap(x);
		if(strcmp(x.maHV,"kk")==0) break;
		Add(L,x);
	} while (true);
	
}

void Xuat(List L){
	cout<<"==========================================================================\n";
	cout<<"DANH SACH HOC VIEN\n";
	cout<<"==========================================================================\n";
	cout<<setw(5)<< left<<"STT";
	cout<<setw(12)<< left<<"Ma HV";
	cout<<setw(20)<< left<<"Ten HV";
	cout<<setw(12)<< left<<"Gioi tinh";
	cout<<setw(15)<< left<<"Diem" <<'\n';
	for(int i=0; i<=L.cout; i++){
		cout<<setw(5) <<left <<i+1;
		cout<<setw(12) <<left <<L.a[i].maHV;
		cout<<setw(20) <<left <<L.a[i].tenHV;
		cout<<setw(12) <<left <<L.a[i].gioiTinh;
		cout<<setw(15) <<left <<L.a[i].diem <<'\n';
	}
	cout<<"==========================================================================\n";
}

bool SoSanhMang(char a[], char b[]) {
	for(int i=0; i<10; i++)
		if(a[i] != b[i])
			return false;
	return true;
}

int TimTheoMaHV(List &L, char mahv[]){
	if(Empty(L)) {
		cout<<"Danh sach trong !!!\n";
		return -1;
	}
	for(int i=0; i<=L.cout; i++)
		if(SoSanhMang(L.a[i].maHV,mahv))
			return i;
}

bool Them(List &L) {
	if(Full(L)) {
		cout<<"Danh sach da day khong the them !!!!\n";
		return false;
	}
	HocVien hv;
	cout<<"Nhap thong tin hoc vien can them:\n";
	Nhap(hv);
	Add(L, hv);
	return true;
}

bool Sua(List &L){
	HocVien hv;
	cout<<"Nhap ma hoc vien muon sua:";
	fflush(stdin);
	gets(hv.maHV);
	int id = TimTheoMaHV(L, hv.maHV);
	if(id==-1) {
		cout<<"khong ton tai hoc vien !!\n";
		return false;
	}

	cout<<"Nhap thong tin hoc vien can sua:\n";
	Nhap(hv);

	fill(begin(L.a[id].maHV), end(L.a[id].maHV), ' ');
	copy(begin(hv.maHV), end(hv.maHV), L.a[id].maHV);
	L.a[id].tenHV = hv.tenHV;
	L.a[id].gioiTinh = hv.gioiTinh;
	L.a[id].diem = hv.diem;
	return true;
}

bool Xoa(List &L) {
	cout<<"Nhap ma hoc vien can xoa: "; char mahv[10]; fflush(stdin); gets(mahv);
	int id = TimTheoMaHV(L, mahv);
	if(id == -1) {
		cout<<"Hoc vien khong ton tai !!\n";
		return false;
	}
	cout<<id<<"::\n";
	for(int i=id; i<L.cout; i++)
		L.a[i] = L.a[i+1];
	L.cout--;
	return true;
}

bool Chen(List &L) {
	if(Full(L)) {
		cout<<"Danh sach day !!!!";
		return false;
	}
	int id;
	cout<<"Nhap vi tri thu tu muon chen: ", cin>>id; id--;
	cout<<"Nhap thong tin hoc vien can chen: \n";
	HocVien x;
	Nhap(x);
	for(int i=L.cout+1; i>id; i--)
		L.a[i] = L.a[i-1];
	L.a[id] = x;
	L.cout++;
}

List listHV;
int main() {
	NhapDS(listHV);
	Xuat(listHV);

	// char ma[10];
	// cout<<"Nhap ma tim:";
	// fflush(stdin);
	// gets(ma);
	// cout<<"Vi tri: "<<TimTheoMaHV(listHV,ma)<<'\n';
	// if(Sua(listHV)) Xuat(listHV);
	if(Xoa(listHV)) Xuat(listHV);
	// if(Them(listHV)) Xuat(listHV);
	// if(Chen(listHV)) Xuat(listHV);
}