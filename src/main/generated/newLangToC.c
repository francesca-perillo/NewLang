#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <stdbool.h>
#include <string.h>

#define MAXDIM 100



/* ------------------------------------------ */
/* --------- FIRMA DELLE FUNZIONI ----------- */
/* ------------------------------------------ */

float sommac(int ,int ,float ,char* *);
void stampa(char* );void esercizio(void);


/* ------------------------------------------ */
/* ---- DICHIARAZIONI VARIABILI GLOBALI ----- */
/* ------------------------------------------ */
int c=1;
void stampa(char *messaggio) {
int a;
int i;
for (int x = 4;x != 1 ;x--){
printf(" \n");
}
printf("%s \n", messaggio);
}


/* ------------------------------------------ */
/* ------ DICHIARAZIONI DELLE FUNZIONI ------ */
/* ------------------------------------------ */
float sommac(int a,int d,float b,char **size) {
float result;
result=a+b+c+d;
if (result>100){
char valore[7]="grande";
*size = valore;
} else {
char valore[8]="piccola";
*size = valore;
}
return result;
}


/* ------------------------------------------ */
/* Copia della funzione main che può essere   */
/* richiamata anche da altre funzioni.        */
/* ------------------------------------------ */
void  esercizio(void){
int x=3;
float b=2.2;
int a=1;
char ans1_tmp[MAXDIM]="";
char taglia_tmp[MAXDIM]="";
char *ans1=ans1_tmp, *taglia=taglia_tmp;
char ans[3]="no";
float risultato=sommac(a,x,b,&taglia);
stampa("la somma  incrementata  è ");
printf("%s \n", taglia);
stampa(" ed è pari a ");
printf("%f \n", risultato);
printf("vuoi continuare? (si/no) - inserisci due volte la risposta \n");
scanf("%s", ans);
scanf("%s", ans1);
while(strcmp(ans, "si") == 0){
printf("inserisci un intero:");
scanf("%d", &a);
printf("inserisci un reale:");
scanf("%f", &b);
risultato=sommac(a,x,b,&taglia);
stampa("la somma  incrementata  è ");
printf("%s \n", taglia);
stampa(" ed è pari a ");
printf("%f \n", risultato);
printf("vuoi continuare? (si/no):");
scanf("%s", ans);
}
printf(" \n");
printf("ciao");
}

/* ------------------------------------------ */
/*               MAIN FUNCTION                */
/* ------------------------------------------ */
int main(void){
int x=3;
float b=2.2;
int a=1;
char ans1_tmp[MAXDIM]="";
char taglia_tmp[MAXDIM]="";
char *ans1=ans1_tmp, *taglia=taglia_tmp;
char ans[3]="no";
float risultato=sommac(a,x,b,&taglia);
stampa("la somma  incrementata  è ");
printf("%s \n", taglia);
stampa(" ed è pari a ");
printf("%f \n", risultato);
printf("vuoi continuare? (si/no) - inserisci due volte la risposta \n");
scanf("%s", ans1);
scanf("%s", ans);
while(strcmp(ans, "si") == 0){
printf("inserisci un intero:");
scanf("%d", &a);
printf("inserisci un reale:");
scanf("%f", &b);
risultato=sommac(a,x,b,&taglia);
stampa("la somma  incrementata  è ");
printf("%s \n", taglia);
stampa(" ed è pari a ");
printf("%f \n", risultato);
printf("vuoi continuare? (si/no):");
scanf("%s", ans);
}
printf(" \n");
printf("ciao");
}