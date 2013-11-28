Úkolníček pro Android
=====================

Popis aplikace
--------------
Aplikace pro evidenci úkolů pro mobilní telefony s operačním systémem Android.

Požadavky
----------
* Uživatelé se do aplikace přihlašují přes uživatelské jméno a heslo
* Data budou ukládána do databáze
* Vytvářet, editovat, mazat a dokončovat úkoly
* Vytvářet, editovat, mazat kategorie úkolů
* Je možné založit úkol pro jiného uživatele
* Notifikace (notifikace na liště doprovázená zvukem a vibrací) uživatele, po přihlášení do aplikace, že mu byl založen nový úkol

Struktura
---------
*Úkol:*
  1. Každý úkol patří právě do jedné kategorie
  2. Obsahuje: název, popis, datum do kdy má být splněn, datum kdy byl splněn, uživatele který ho založil a uživatele který ho splnil
  3. Název úkolu je povinný parametr
  4. Datum do kdy má být úkol splněn je nepovinný parametr

*Kategorie:*
  1. Je definována pouze názvem
  2. Může obsahovat 0-N úkolů

Aplikační omezení
-----------------
* Kategorie, které neobsahují úkoly, je možné upravovat/mazat
* Upravovat úkol lze pouze tehdy, pokud není uzavřen
* Odstranit úkol lze pouze tehdy, jedná-li se o úkol mnou založený
* Úkoly delegované na mne nemohu odstranit a ani jim nemohu změnit datum, do kdy mají být dokončeny
* Seznam všech uživatelů a kategorií je dostupný všem
* Přihlášený uživatel vidí jen úkoly: své vlastní (založil je pro sebe), delegované na něj (někdo založil pro mne), delegované někomu jinému (založil jsem někomu jinému)

Použité technologie
-------------------

Instalace
---------