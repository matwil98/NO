void Sortuj(int *T,int n){
int i,j,tmp;
if(n<2) return;
for(i=0;i<n-1;i++){
for(j=i+1;j<n;j++){
	if(T[i]>T[j]{
		tmp = T[i];
		T[i]=T[j];
		T[j] = tmp;
	}
	}
	}
	}