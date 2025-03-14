Project Y - Profilverwaltung
=======
# Beschreibung
Im Rahmen des Gruppenprojekts in "Verteilte Systeme" haben wir einen Microblogging-Service entwickelt, inspiriert von Twitter. Meine Komponente fokussiert sich auf die Profilverwaltung. Die technische Umsetzung erfolgte mit **Angular** als Framework und **Spring Boot** für das Backend.
## Funktionen
Die Login-Komponente bietet folgende Features:
Bei meiner Komponente handelt es sich um die Profilverwaltung, dass heißt Änderungen von den essentiellen Nutzerdaten wie Vorname, Nachname, Email, Username und Passwort sowie der Upload von einem Profilbild. Zudem ist auch eine Löschung des Accounts möglich. Um sinnvoll eine solche Komponente zu implementieren waren auch eine Registrierung und Login Komponente notwendig da jeder User natürlich nur seine eigenen Attribute ändern kann, seinen eigenen Account löschen, etc. Da Registrierung und Login nicht die eigentliche Hauptaufgabe waren sind sie nicht vollständig ausprogrammiert, zum Beispiel gibt es bei einer fehlerhaften Registrierung teilweise nur generische Fehlermeldungen.
Out-Of-Scope ist das Anzeigen eigener Tweets, das beim „Quality-Gate-Gespräch“ als Option beschrieben wurde sollte der Umfang sich andernfalls als zu gering herausstellen. Aufgrund des zusätzlichen Aufwands für die Registrierung/Login wurde sich dagegen entschieden die eigenen Tweets im Profil anzuzeigen.
## Umsetzung der Komponente
Bei der Erstellung der Komponente wurde größtenteils sich an die Entscheidungen aus dem ACD gehalten und für das Backend Spring Boot genutzt sowie für das Frontend Angular. Bei der Datenbank wurde aufgrund von Kostengründen auf die Oracle-Datenbank für den Prototypenverzichtet und stattdessen eine PostgreSQL-Datenbank eingesetzt. Der Prototyp wird im Gegensatz zur endgültigen Anwendung nicht gehostet. Ebenso wurden Tools wie Docker, Kubernetes und Flyway bei dem Prototypen nicht berücksichtigt.
Die Komponente wurde mit einer Schichtenarchitektur programmiert und nicht als Microservice, da wie im ACD beschrieben ein Minimum Viable Product (MVP) implementiert werden sollte.


## Schnittstellen
Die Komponenten wurden bewusst separat entwickelt und abgegeben, ohne eine vollständige Verknüpfung im Rahmen dieses Projekts umzusetzen. 
- **Tweet-Inhaltsupload**
  - **Funktion**: Ermöglicht das Hochladen neuer Tweets und deren Anzeige in einer Übersicht.
  - **Schnittstelle**: Zugriff auf das Profil über Profil-Button an der linken Leiste, könnte über REST-API direkt zur /profile Seite meiner Profilkomponente weitergeleitet werden

- **Tweet-Interagieren**
  - **Funktion**: Verwaltet Interaktionen wie Likes, Retweets und Kommentare zu Tweets.
  - **Schnittstelle**: Zugriff auf das Profil über Profil-Button an der linken Leiste, könnte über REST-API direkt zur /profile Seite meiner Profilkomponente weitergeleitet werden

- **Login/Registrierung**
  - **Funktion**: Erlaubt es Usern sich zu registrieren/einzuloggen
  - **Schnittstelle**: Die provisorische Autorisierung der User in meiner Komponente würde wegfallen und durch die Autorisierung bei Tizian ersetzt. Die Frontend-Seiten /register und /login sind somit nur für den Prototypen relevant.

## Voraussetzungen und Installation
1.	Benötigte Software:
•	[Java 17+]
•	[Node.js + npm] 
•	[Angular CLI]
•	[PostgreSQL]
•	[Git]

2.	Datenbank vorbereiten:
•	Die PostgreSQL-Datenbank muss manuell erstellt werden, bevor die Anwendung gestartet wird (zum Beispiel über pgAdmin): 
CREATE DATABASE Twitter;
CREATE USER postgres WITH ENCRYPTED PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE Twitter TO postgres;
(siehe application.yml im Backend)
3.	 Backend starten 
--> Repository klonen und Run-Button betätigen
4.	Frontend starten
--> Repository klonen und Run-Button betätigen
5.	Über GUI einen User registrieren
6.	Funktionen der GUI austesten
