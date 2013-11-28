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
* Na straně databáze jsem použil SQL Server 2005 Express
* Aplikační server je JBoss 7.1.1
* Klient psán v Android API 19

Instalace
=========

Databáze
--------
1. Stáhnout a nainstalovat [SQL Server 2005 Express](http://www.microsoft.com/en-us/download/details.aspx?id=21844)
2. Pro správu databáze používám Microsoft SQL Server Management Studio Express dostupný [zde](http://www.microsoft.com/en-us/download/details.aspx?id=8961)
3. Po instalaci překontrolovat:
    * Zda je SQL server spuštěný
    * Zda jse zapnutá služba *SQL Server Browser*
    * Zda je povoleno TCP/IP spojení na portu 1433
4. Na disku C:\ si založte adresář *TaskManager* a v něm edresář *db*. Do tohoto adresáře bude založena databáze (soubor TaskManager.mdf).
5. Script pro založení databáze je [zde](task-manager-db/CreateDatabaseScript.sql)

   Script založí databázi s názvem *TaskManager* do připraveného adresáře z bodu 4. 
   Založí aplikačního uživatele *taskManager_app* pomocí kterého se bude JBoss připojovat
   Do databáze založí tabulky LOGIN (tabulka uživatelů), TSK (tabulka úkolů) a TSK_CATEG (tabulka kategorií)
   Založí dvě kategorie: *Pracovni* a *Domaci*
   Založí tři uživatele:
      Petr (uživatelské jméno: petr, heslo: taskmanager)
      Martin (uživatelské jméno: martin, heslo: taskmanager)
      Tomas (uživatelské jméno: tomas, heslo: taskmanager)