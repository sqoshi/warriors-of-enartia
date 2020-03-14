#Warrios of Enartia

Gra 'Fabularna' polegająca na rozwijaniu własnego bohatera.
Każdy gracz tworzy własne konto, rejestrowane w bazie danych. Dane rejestracji są weryfikowane, przez odpowiedni podsystem. 
Na utworzonym koncie o unikalnym loginie tworzy się herosa w jednej z wybranych klas na ten moment zaimplementowant jest Archer, Mage i Warrior.
Heroes początkowo otrzymuje podstawowy ekwipunek, który jest wystarczający na kilka podstawowych wyprawych, w których gracz radzi sobie stosunkowo do klasy.
Gracz wysyła swojego heroesa na wyprawę. Aby móc wziąc udział w wyprawie musi nie brać udziału w innej oraz musi mieć wystarczająco dobry ekwipunek. Tym trudniejsza misja tym lepszy ekwipunek musi posiadać gracz.
Po ukończezniu misji gracz otrzymuje odpowiednia ilość surowcy np. złota, które może wymienić na inny ekwipunek w sklepie.
W grze jest dostępny ranking herosow oparty na ich ataku i defensywnie. Ranking postaci jest ogólny lub klasowy oraz oddzielny ranking przedmiotów.

Interfejs graficzny GUI aplikacji został napisany w Javie (Swing).
Część bazodanowa składa się z relacyjnej bazy danych MySQL.
Komunikacja pomiędzy powyższymi technologami jest zapewniona dzięki interfejsowi oprogramowania JDBC, technice 'Connection Pooling', która ogranicza ilość kosztownych połączeń z bazą do minimum.
Aplikacja składa się z wielu podsystemów:-
System Rejestracji Kont.
System Logowania.
Ranking herosów - całkowity, klasowy.
Ranking przedmiotów - osobny dla każdego rodzaju przedmiotu.
Panel gracza
Panel Administratora :-
  -procedura wprowadzania nowych zestawów broni
  -procedura wprowadzenia nowych zestawów defensywnych helmets, shields, armors..
  -procedura usunięcia starego setu
  -procedura usunięcia konta gracza ( System kar - banów)
  -procedura usunięcia postaci( System kar - banów)
  -procedura przydzielenia konkretnego przedmiotu, np dla najlepszego gracza w miesiącu
  -procedura przydzielenia surowca dla gracza.
 
W celu zapewnienia spójnej funkcjonalności i optymalizacji bazy danych zostały użyte constrainty, triggery, zapytania dynamiczne, indexy, transakcje, backup&restore.
Między innymi :
Procedury:-
  -insertWeaponSet(cursor),
  -insertClassSet,
  -fundamental_class_insert,
  -deleteClassSet.
Triggery:-
  -logincheck,
  -wearUpNewHeroes
Transkacje:-
  -basicSets
indexy, constrainty przy tworzeniu tabeli.

                      
