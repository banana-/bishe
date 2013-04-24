#include <stdio.h>
#define MAX  100
int partition(int num[], int begin, int end){
	int key = num[begin];
	int i = begin, j = end;
	while(i < j){
		while(i < j && num[j] >= key) j--;
		if(i < j){
			num[i++] = num[j];
		}
		while(i < j && num[i] <= key) i++;
		if(i < j){
			num[j--] = num[i];
		}
	}
	num[i] = key;
	return i;
}
	
//lsdfjlsdakfj
/**fsdlak;fjds
fsddsf
*/
/* */
void qsort(int num[], int begin, int end){
	int m;
	if(begin < end){
		m = partition(num, begin, end);
		qsort(num, begin, m - 1);
		qsort(num, m + 1, end);
	}
}

int main(){
	int num[MAX], n;
	while(scanf("%d", &n) != EOF){
		int i;
		for(i = 0; i < n; i++){
			scanf("%d", num+i);
		}
		qsort(num, 0, n-1);
	}

}
