#include<bits/stdc++.h>

using namespace std;

#define MAX 6
struct hanghoa{
	char mahang[30];
	char tenhang[30];
	char donvitinh[20];
	int dongia;
	int soluong;
};

struct List{
	int count;
	hanghoa a[MAX];
};

void create(List &L){
	L.count = -1;
}

int full(List L){
	return (L.count == MAX - 1);
}

int empty(List L){
	return (L.count == -1);
}

int add(List &L, hanghoa x){
	if(!full(L)){
		L.count++;
		L.a[L.count] = x;
		return 1;
	}
	else{
		return 0;
	}
}



void nhap(hanghoa &x){
	cout << "Nhap ma hang: "; fflush(stdin); gets(x.mahang);
	cout << "Nhap ten hang: "; fflush(stdin); gets(x.tenhang);
	cout << "Nhap don vi tinh: "; fflush(stdin); gets(x.donvitinh);
	cout << "Nhap don gia: "; cin >> x.dongia;
	cout << "Nhap so luong: "; cin >> x.soluong;
}

void nhapds(List &L){
	create(L);
	hanghoa x;
	int i = 1;
	cout << "Nhap e de ket thuc\n";
	cout << "Nhap thong tin hang hoa: \n";
	while(true){
		cout << "Nhap thong tin mat hang thu " << i << ": \n";
		
		cout << "Nhap ma hang: "; fflush(stdin); gets(x.mahang);
		if(strcmp(x.mahang,"e") == 0){
			return;
		}
		cout << "Nhap ten hang: "; fflush(stdin); gets(x.tenhang);
		cout << "Nhap don vi tinh: "; fflush(stdin); gets(x.donvitinh);
		cout << "Nhap don gia: "; cin >> x.dongia;
		cout << "Nhap so luong: "; cin >> x.soluong;
		
		
		
		
		
		if(!add(L,x)){
			cout << "danh sach da day\n";
			return;
		}
		i++;
	}
}


void xuat(List &L){
	cout << setw(15) << "stt" << setw(15) << "ma hang" << setw(15) << "ten hang"  << setw(15) << "don vi tinh" << setw(15) << "dong gia" << setw(15) << "so luong" << setw(15) << "thanh tien" << endl;
	for(int i =0; i <= L.count; i++){
		int thanhtien = L.a[i].dongia * L.a[i].soluong;
		cout << setw(15) << i + 1 << setw(15) << L.a[i].mahang << setw(15) << L.a[i].tenhang << setw(15) << L.a[i].donvitinh << setw(15) << L.a[i].dongia << setw(15) << L.a[i].soluong << setw(15) << thanhtien<< endl;
	}
}

void timkiem(List L){
	int slmin = L.a[0].soluong;
	int slmax = L.a[0].soluong;
	for(int i = 0; i <= L.count; i++){
		if(L.a[i].soluong < slmin){
			slmin = L.a[i].soluong;
		}
		if(L.a[i].soluong > slmax){
			slmax = L.a[i].soluong;
		}
	}
	
	cout << "phan tu co so luong nho nhat la: \n";
	cout << setw(15) << "stt" << setw(15) << "ma hang" << setw(15) << "ten hang"  << setw(15) << "don vi tinh" << setw(15) << "don gia" << setw(15) << "so luong" << setw(15) << "thanh tien" << endl;
	for(int i = 0; i <= L.count; i++){
		if(L.a[i].soluong == slmin){
		int thanhtien = L.a[i].dongia * L.a[i].soluong;
		cout << setw(15) << i + 1 << setw(15) << L.a[i].mahang << setw(15) << L.a[i].tenhang << setw(15) << L.a[i].donvitinh << setw(15) << L.a[i].dongia << setw(15) << L.a[i].soluong << setw(15) << thanhtien<< endl;
		}
	}
	
	cout << "\n phan tu co so luong lon nhat la: \n";
	cout << setw(15) << "stt" << setw(15) << "ma hang" << setw(15) << "ten hang"  << setw(15) << "don vi tinh" << setw(15) << "don gia" << setw(15) << "so luong" << setw(15) << "thanh tien" << endl;
	for(int i = 0; i <= L.count; i++){
		if(L.a[i].soluong == slmax){
		int thanhtien = L.a[i].dongia * L.a[i].soluong;
		cout << setw(15) << i + 1 << setw(15) << L.a[i].mahang << setw(15) << L.a[i].tenhang << setw(15) << L.a[i].donvitinh << setw(15) << L.a[i].dongia << setw(15) << L.a[i].soluong << setw(15) << thanhtien<< endl;		}
	}
}


void thaythe(List &L){
	hanghoa x;
	cout << "Nhap thong tin mat hang thay the: \n";
	nhap(x);
	
	cout << "Nhap ten mat hang can thay the: ";
	char tentt[30]; fflush(stdin); gets(tentt);
	for(int i = 0; i <= L.count; i++){
		if(strcmp(tentt,L.a[i].tenhang) == 0){
			L.a[i] = x;
		}
	}
}

int demdgcao(List &L){
	int lmax = L.a[0].dongia;
	for(int i = 0; i <= L.count; i++){
		if(L.a[i].dongia >= lmax){
			lmax = L.a[i].dongia;
		}
	}
	int dem = 0;
	for(int i = 0; i <= L.count; i++){
		if(L.a[i].dongia == lmax ){
			dem++;
		}
	}
	return dem;
	
}

void chen(List&L){
	cout << "Nhap thong tin ma hang can chen: \n";
	hanghoa x;
	nhap(x);
	int k; cout << "Nhap vi tri chen: "; cin >> k;
	--k;
	if(k >= 0 && k <= L.count + 1){
		if(!full(L)){
			for(int i = L.count; i >= k; i-- ){
				L.a[i + 1] = L.a[i];
			}
			L.a[k] = x;
			L.count++;
		}
		else{
			cout << "danh sach day, khong the chen\n";
		}
	}
	else{
		cout << "vi tri chen khong hop le \n";
	}
}

void xoa(List &L){
	int k; cout << "Nhap vi tri can xoa: "; cin >> k;
	k--;
	if(k >= 0 && k <= L.count){
		if(!empty(L)){
			for(int i = k; i < L.count; i++){
				L.a[i] = L.a[i + 1];
			}
			L.count--;
		}
		else{
			cout << "danh sach rong khong the xoa\n";
		}
	}
	else{
		cout << "vi tri xoa khong hop le \n";
	}
}


void sapxep(List &L){
	for(int i = 0; i <= L.count - 1; i++){
		int minpos = i;
		for(int j = i + 1; j <= L.count; j++){
			if(L.a[minpos].dongia * L.a[minpos].soluong > L.a[j].dongia * L.a[j].soluong)
			minpos = j;
		}
		swap(L.a[i], L.a[minpos]);
	}
}

int main(){
	List L;
	nhapds(L);
	xuat(L);
	
	cout << "\n---TIM KIEM MIN MAX---\n";
	timkiem(L);
	
	cout <<" \n---THAY THE---\n";
	thaythe(L);
	xuat(L);
	
	cout << "\n---SO MAT HANG CO CUNG DON GIA CAO NHAT---\n";
	cout << demdgcao(L);
	
	cout << "\n---CHEN----\n";
	chen(L);
	xuat(L);
	
	cout << "\n\n\n---XOA---\n";
	xoa(L);
	xuat(L);
	
	
	cout << "\n\n---SAP XEP THANH TIEN---\n";
	sapxep(L);
    xuat(L);
    return 0;
}