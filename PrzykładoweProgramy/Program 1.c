#include <stdio.h>
#include <conio.h>
unsigned long suma;
unsigned int liczba;
unsigned int tablica[100];
int licznik;
main () {
   printf("Program wczytuje maksymalnie 100 liczb naturalnych i oblicza ich sume.\n");
   licznik = 0; //zainicjowanie wartoœci zmiennych
   suma = 0;
   liczba = 0;
     
   for( ; ; )    // nieskoñczona pêtla for. Mo¿na j¹ przerwaæ poleceniem break 
  {
      if ((liczba == 1) || (licznik == 100))  break;
      else
         {
            printf("Podaj liczbe dodatnia z zakresu: 2 ... 65535.\n");
            printf("Podaj wartosc 1, aby wczesniej zakonczyc dzialanie programu.\n");
            scanf("%u", &liczba);  //wczytanie liczby z klawiatury
            tablica [licznik] = liczba; licznik++;
            suma = suma + liczba;
        } 
  }
  printf("Wczytano %d liczby. Ich suma wynosi: %lu\n", licznik -1, suma-1);
  getche();
  return 0;
}
