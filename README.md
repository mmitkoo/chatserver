# Chat Server

### Чат клиент-сървър със следната функционалност:
- Да обслужва много потребители едновременно
- Всеки потребител има уникално име, с което се индентифицира при свърване към сървъра
- Потребителите могат да напуснат чата по всяко време
- Потребителите могат да изпращат лични съобщение до друг активен (т.е. в момента свързан към сървъра) потребител, както и съобщения до всички активни потребители
- Извеждане на всички активни потребители

### Chat Server

- Сървърът трябва да може да обслужва множество клиенти едновременно
- Сървърът получава команди от клиентите и връща подходящ резултат

### Описание на клиентските команди

```bash
- connect <host> <port> <username> - свързва потребител с дадено потребителско име към съвръра
- disconnect – напуска чата
- list-users – извежда списък с всички активни в момента потребители
- send <username> <message> - изпраща лично съобщение до даден активен потребител
- send-all <message> - изпраща съобщение до всички активни потребители
```

### Пример

```bash
# start a client from command line
$ java bg.sofia.uni.fmi.mjt.chat.ChatClient
connect localhost 8080 java-duke
=> connected to server running on localhost:8080 as java-duke
# or
=> cannot connect to server on localhost:8080, make sure that the server is started

send java-duke hi there
=> [12.01.2018 20:05] java-duke: hi there
=> [12.01.2018 20:06] adam: hallo # sent to java-duke from adam
# or
=> java-duke seems to be offline

list-users
=> john, connected at 12.01.2019 20:05
=> adam, connected at 12.01.2019 21:12
# or
=> nobody is online

disconnect
=> disconnected from server on localhost:8080
# or
=> cannot disconnect, try to connect first
```