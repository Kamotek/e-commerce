Init

TODO
BUS < czy cos takiego mi sie kojarzy
Ogarnac temat rabbitmq dla katalogu i ogarnac tez rabbitmq dla wczytywania elementow katalogu
Ogarnac resztze mikroserwisow, powinno pojsc gladko
zatsnaowic sie nad zintegrowaniem ich i czym to sie rozni

api versioning

pomysly na asynchroniczna komunikacje:

- gdy powstaje order to odliczamy odpowiednia ilosc rzeczy z inventory
- gdy powstaje order to wysylamy notyfikacje
- gdy dodajemy nowy produkt do magazynu, to ma on trafic rowniez do catalogu

- gdy rejestrujemy uzytkownika to wysylamy mu maila




- gdy tworzony jest order, to wysylane jest na kolejke payment zapytanie o platnosc
- platosc widzi event na kolejce, przetwarza i tworzy blizniaczy event o nazwie obsluzone
- order nasluchuje i widzi ze zostalo zaplacone


docker build -t auth-service .
docker tag auth-service:latest 655449637944.dkr.ecr.eu-north-1.amazonaws.com/inventory-service:latest
docker push 655449637944.dkr.ecr.eu-north-1.amazonaws.com/inventory-service:latest






/*
db_endpoint = "ecommerce-postgres.cdqs0coygfjv.eu-central-1.rds.amazonaws.com"
db_port = 5432
rabbitmq_endpoint = "amqps://b-2f61931b-989a-4ca9-9434-2fad097dd34c.mq.eu-central-1.on.aws:5671"
rabbitmq_port = 5671

*/
