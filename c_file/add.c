void add(int a[], int b[], int n){
	int i,j;
	j = 3;
	for(i = 0; i < n; i = i + 1){
		if(i < j){
			a[i] = b[i] + a[i];	
		}else{
			a[i] = a[i] - b[i];	
		}
	}
}
