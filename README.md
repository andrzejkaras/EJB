# EJB student project

Client send notification to Front Bean(Remote, Stateless, Asynchronous Bean).
Front bean sends notification to Middle Bean (Singleton). 
Middle bean receives notification and creates file with specified file name and it's content.
Back bean (Startup Singleton) wakes up every ten seconds and reads all files from given directory, saves data to database and deletes files.
