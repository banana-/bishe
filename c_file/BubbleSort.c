void bubble_sort(int num[], int n){
	int i,j, temp;
		
	for( i = 0; i < n - 1; i = i + 1)
	{
		for( j = 0; j < n - i - 1; j=j+1)
		{
				
			if( num[j] > num[j + 1] )
			{
				temp = num[j];
				num[j] = num[j + 1];
				num[j + 1] = temp;
			}else{
				//temp = 0;
			}
		}
		
	}
	
}
