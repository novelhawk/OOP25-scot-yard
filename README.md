# Progetto Programmazione ad Oggetti

Progetto universitario per il corso di Programmazione ad Oggetti dell'Università di Bologna.

## Requisiti

- Java 21
- Gradle ??

#Map render REF.(commit: 6cf70ea)

Model-View-Controller. Il punto di
ingresso dell'applicazione, classe ScotlandYard, che
contiene il metodo main. Si istanziano i tre componenti principali dell'architettura MVC,
passando al
controller i riferimenti al model e alla view. Una volta creati questi
oggetti, viene invocato il metodo launch del controller, delegando a
quest'ultimo l'intera gestione del flusso applicativo.

Il controller principale, implementato dalla classe ControllerImpl, agisce
come coordinatore centrale dell'applicazione. Quando viene chiamato il
metodo launch, il controller non avvia direttamente il gioco ma crea un
GameLauncherController, passandogli la view e una callback che verrà
eseguita quando l'utente avrà selezionato la risoluzione desiderata.
Questa callback è il metodo run del controller stesso, che accetta come
parametro un oggetto Size rappresentante la risoluzione scelta. Il
GameLauncherController si occupa quindi di gestire la finestra di
selezione della risoluzione, un'interfaccia grafica che mostra all'utente
tutte le risoluzioni disponibili e compatibili con il suo schermo.

La gestione delle risoluzioni:
Il sistema mantiene una lista di risoluzioni
predefinite nella classe Constants, dove sono elencate le dimensioni
standard supportate dall'applicazione.
Quando il GameLauncherController viene inizializzato, interroga la view per ottenere
la risoluzione massima dello schermo dell'utente attraverso il metodo
getMaxResolution, che utilizza le API di AWT per recuperare le dimensioni
fisiche del display. Una volta ottenuta questa informazione, il controller
filtra la lista delle risoluzioni predefinite, mantenendo solo quelle che
possono essere visualizzate completamente sullo schermo dell'utente. Se
per qualche motivo nessuna risoluzione risulta compatibile, il sistema
utilizza come fallback la risoluzione più piccola disponibile.
Il controller seleziona automaticamente come default la risoluzione centrale
nell'elenco filtrato, fornendo un punto di partenza ragionevole per
l'utente.

La view del launcher, implementata in GameLauncherViewImpl, è una finestra
Swing di dimensioni fisse che presenta un'interfaccia minimale con il
titolo del gioco, una combo box per la selezione della risoluzione e un
pulsante per avviare il gioco. Quando l'utente clicca sul pulsante di avvio,
la view notifica il controller della selezione, che a sua volta invoca la
callback passata durante l'inizializzazione, chiudendo poi la finestra
del launcher.

// IRE
Quando la callback viene eseguita, il ControllerImpl memorizza la 
risoluzione selezionata e crea una nuova finestra, che sarà poi la
finestra principale, in cui verrà mostrato il menù e anche il gioco
vero e proprio. Una volta creata questa, viene caricato il menù principale,
attraverso la creazione di un controller intermedio : MainMenuController.
Questo controller ha il compito di gestire il menu principale del
gioco. Al suo interno viene creata la MainMenuView nel metodo getMainPanel
del relativo controller, che restituisce il JPanel creato dalla view e che viene
restituito dal metodo getMainPanel della MainMenuView. In questo JPanel sono 
presenti 4 JButton : "Nuova partita" permette di iniziare una nuova partita, 
"Carica partita" permette di caricare una delle partite salvate (//TODO), 
"Statistiche" permette di visualizzare la partita più lunga e il numero di vittorie 
e sconfitte nelle varie partite (//TODO), "Esci" che chiude l'applicazione. 
Il controller principale usa questo JPanel, tramite il metodo getMainPanel del
MainMenuController, come argomento per il metodo displayPanel che, al suo interno, 
richiama il metodo displayPanel della view principale, prendendo sempre come 
argomento un JPanel. 

//IRE
Con la pressione del JButton "Nuova partita" viene chiamato il metodo
newGameMenu del MainMenuController, in cui viene chiamato il metodo
loadNewGameMenu del contoller principale. In questo viene creato un nuovo 
controller intermedio, ovvero il NewGameMenuController, che si occupa della 
gestione del menù per creare una nuova partita. Analogamente al main menu, viene
creata la NewGameMenuView nel relativo controller nel metodo getMainPanel, che 
restituisce il JPanel creato dalla view e che viene restituito dal metodo getMainPanel
della view. In questo JPanel l'utente inserisce il proprio nome e poi seleziona : la
modalità di gioco (Mister X o Detective) e il livello di difficoltà (facile, medio, 
difficile). Sono presenti due JButton : "Torna indietro" invoca il metodo loadMainMenu 
del controller princiaple, riportando al main menu; "Avvia gioco" invoca il metodo
startGame del controller principale, passando tre argomenti ovvero la modalità di gioco
selezionata, il livello di difficoltà selezionato e il nome del giocatore.

Il metodo startGame rappresenta il punto di svolta dove l'applicazione
passa dalla fase di configurazione alla fase di gioco vera e propria. La
prima operazione che viene eseguita è l'inizializzazione del model, che
fino a questo momento era rimasto inattivo. Quando viene invocato il
metodo initialize del ModelImpl, questo crea un'istanza di MapReader, una
classe dedicata alla lettura e nel parsing dei dati della mappa dal
formato JSON. Il MapReader carica il file di default della mappa, che si
trova nelle risorse del progetto nel percorso
it/unibo/scotyard/model/map/ScotlandYardMap.json.

Il processo di lettura del JSON è gestito attraverso la libreria Gson di
Google. Il MapReader apre uno stream di input dal classpath delle risorse,
crea un reader e utilizza Gson per parsare il
contenuto JSON in un JsonObject. La struttura del file JSON è ben definita
e contiene diverse sezioni fondamentali. La sezione nodes contiene un
array di oggetti, dove ciascun oggetto rappresenta una stazione sulla
mappa con un identificativo numerico univoco e coordinate x e y che
definiscono la posizione logica della stazione nello spazio cartesiano.
Queste coordinate non rappresentano pixel sullo schermo, ma piuttosto
unità in uno spazio logico di riferimento che verrà successivamente
scalato per adattarsi alla finestra di gioco.

La sezione connections definisce tutti i collegamenti tra le stazioni,
specificando per ciascuno il nodo di partenza, il nodo di arrivo e il tipo
di trasporto utilizzabile su quella connessione. I tipi di trasporto sono
definiti dall'enum TransportType e includono TAXI, BUS, UNDERGROUND e
FERRY. Alcune connessioni possono avere anche un array di waypoints,
ovvero nodi intermedi attraverso cui la linea di collegamento deve passare
per (effeto grafico piu decente), e fedele alla mappa originale.
La sezione revealTurns contiene un array di numeri interi
che indicano in quali turni la posizione di Mr. X viene rivelata ai
detective //TODEFINE.
Infine, la sezione initialPositions definisce le possibili
posizioni iniziali sia per Mr. X che per i detective, anche se attualmente
questa funzionalità non è ancora pienamente utilizzata nel gioco
//TODEFINE.

Il MapReader trasforma tutti questi dati JSON in oggetti Java.
I nodi vengono convertiti in istanze di MapNode, che estendono la
classe di base Node aggiungendo funzionalità specifiche per il model
layer. Le connessioni diventano oggetti MapConnection con logica per
gestire i waypoints e i tipi di trasporto. Tutti questi oggetti vengono
poi aggregati in un'istanza di MapData, che rappresenta l'intera struttura
della mappa con tutti i suoi metadati. MapData è una classe immutabile
che fornisce numerosi metodi di interrogazione per navigare la struttura
della mappa, trovare nodi specifici, ottenere le connessioni da un
determinato nodo, verificare quali nodi sono raggiungibili e controllare
se un turno è un reveal turn.

Una volta che il model ha completato l'inizializzazione e la MapData è
disponibile, il controller procede con l'inizializzazione della view.

Separazione tra il model layer e il view layer attraverso l'uso di Data Transfer Objects.
La classe MapData, che appartiene al package model, non viene passata
direttamente alla view. Invece, viene invocato il metodo info di MapData,
che crea e restituisce un'implementazione di MapInfo.
Questo DTO fornisce un'interfaccia più
ristretta e orientata alla visualizzazione, esponendo solo i metodi
necessari per il rendering senza dare accesso a tutta la logica di
business del model. MapInfo espone stream di nodi e connessioni invece di
liste, permettendo un'elaborazione più efficiente e funzionale dei dati.

//IRE 
Il controller, dopo aver iniziallizato il modello, crea la GameView e il 
GameController. Per creare la prima viene passato come argomento un'implementazione di 
MapInfo, ottenuta chiamando il metodo getMapData.info del Model. La GameView si compone 
di un pannello principale, il MainPanel che è diviso in due parti : il MapPanel, che è 
il cuore della rappresentazione grafica della mappa; e la Sidebar, che permette all'utente 
di tenere traccia di info importanti durante lo svoglimento della partita.

Il MapPanel separa lo spazio logico della mappa dallo spazio fisico dello schermo.
Permette di definire le coordinate dei nodi una volta sola
in uno spazio logico di riferimento, e poi scalare tutto dinamicamente in
base alle dimensioni effettive della finestra. Quando il MapPanel viene
inizializzato, la prima cosa che fa è determinare i limiti dello spazio
logico analizzando tutti i nodi e trovando le coordinate massime in x e y.
Questi valori definiscono la dimensione originale della mappa in unità
logiche.

Ogni volta che la finestra viene ridimensionata, il MapPanel deve
ricalcolare il fattore di scala e gli offset necessari per centrare la
mappa. Questo calcolo avviene nel metodo calculateScaleAndOffset, che
viene invocato in modo lazy durante il rendering solo quando il flag
scaleCalculated è false. Il sistema calcola due fattori di scala separati
per la larghezza e l'altezza, considerando anche i margini da lasciare
intorno alla mappa, e poi utilizza il minore dei due per garantire che
l'intera mappa rimanga visibile senza distorsioni. Una volta determinato
il fattore di scala, vengono calcolati gli offset orizzontali e verticali
necessari per centrare la mappa nello spazio disponibile.

Per ottimizzare le performance, il MapPanel implementa diverse strategie
di caching. Le posizioni scalate di tutti i nodi vengono pre-calcolate e
memorizzate in una mappa quando cambia la dimensione della finestra,
evitando di rieseguire gli stessi calcoli geometrici per ogni frame di
rendering. Anche l'immagine di background, se presente, viene pre-scalata
alla dimensione della finestra in modo da poter essere semplicemente
copiata durante il paintComponent senza dover applicare trasformazioni
costose ad ogni frame. Questa immagine di background è un file PNG che
viene caricato dalle risorse attraverso ImageIO e fornisce un contesto
visivo per la mappa.

Il processo di rendering vero e proprio, che avviene nel metodo
paintComponent, segue una sequenza ben precisa. Prima viene attivato
l'antialiasing e vengono impostate varie hint di rendering per
massimizzare la qualità visiva. Poi viene disegnato il background
pre-scalato, seguito dal titolo della mappa centrato nella parte superiore
della finestra e dalla legenda che spiega i colori dei diversi tipi di
trasporto. Successivamente vengono renderizzate tutte le connessioni tra i
nodi, con una logica particolare per cui le connessioni diverse dai taxi
vengono disegnate prima, e quelle taxi vengono disegnate sopra. Questo
crea un effetto di layering dove le linee gialle dei taxi risultano più
visibili. Infine vengono disegnati i nodi stessi, rappresentati come
cerchi bianchi.

Ogni tipo di trasporto ha caratteristiche visive distintive. I taxi sono
rappresentati da linee gialle sottili continue, i bus da linee verdi
spesse continue, le underground da linee rosse tratteggiate di media
larghezza, e i ferry da linee nere tratteggiate.

I nodi stessi vengono disegnati con una logica che comunica
visivamente quali tipi di trasporto sono disponibili da quella stazione.
Ogni nodo ha un cerchio bianco centrale con il numero della stazione,
circondato da un bordo nero. Se il nodo è una stazione ferry, il bordo è
tratteggiato invece che continuo. Intorno al bordo esterno del nodo,
vengono disegnati archi colorati che rappresentano i tipi di trasporto
disponibili escludendo i taxi, dato che tutti i nodi hanno accesso ai
taxi. Se un nodo ha accesso a bus e underground, avrà due archi
semicircolari, uno verde e uno rosso, che circondano il nodo come una
corona colorata.

//IRE  
La Sidebar, in particolare, contiene il nome del tipo di giocatore attuale 
dell'utente (detective o mister X), il numero del round attuale, l'inventario 
(biglietti) e un button che apre una piccola finestra in cui sono riassunte
le regole principali del gioco (che variano leggermente a seconda dell'attuale
modalità di gioco). In particolare, questa finestra viene aperta dopo la pressione
del button : all'interno della SidebarPanel viene chiamato il metodo displayRulesWindow
della GameView, che prende come argomento il panel da inserire all'interno della 
finestra con le regole e che viene creato nel metodo createRulesPanel della 
SidebarPanel.
//TODO : Aggiungere -> bottoni per giocare 

// IRE  
Una volta che la GameView è stata completamente inizializzata con il MapPanel e 
la Sidebar, viene creato il GameController, che prende in ingresso il model del 
Game, attraverso il metodo getGameData del model, e la GameView. Dopdoiché viene 
impostato l'observer della GameView, che è il GameController e viene chiamato il metodo 
loadGamePanel nel ControllerImpl. Viene subito aggiornata la sidebar, tramite il 
metodo updateSidebar del GameController, che aggiorna correttamente il nome del tipo 
di giocatore dell'utente (a seconda della modalità di gioco), il numero del round e 
l'inventario. (//TODO : aggiunte alla sidebar) 
Tutti questi dati vengono presi dal GameController attraverso dei getter che 
richiamano il model Game. Una volta fatto ciò, viene mostrato il MainPanel della 
GameView tramite il metodo displayPanel della view principale e viene poi chiamato 
il metodo forceLayoutUpdate della view, che prende come parametri il MainPanel e la 
MapPanel della GameView. In questo metodo la view forza un aggiornamento del layout 
utilizzando SwingUtilities.invokeLater per garantire che tutte le operazioni di layout 
e repaint avvengano correttamente sul thread di gestione degli eventi di Swing.
Una volta fatto ciò, si ritorna nel metodo startGame(), in cui era stata effattuata 
la chiamata al metodo loadGamePanel(). 
//TODO : gestione flusso gioco


// IRE  
Per quanto riguarda la gestione della fine partita, in GameImpl è presente il metodo 
isGameOver(), che controlla se la partita è giunta a suo termine, restituendo un valore 
booleano. Questo è vero se è stato raggiunto il numero massimo di round, oppure se 
il detective e Mister X si trovano nella stessa posizione oppure se uno dei bobby e 
Mister X si trovano nella stessa posizione. Nel Controller princiapale, durante lo svolgimento 
della partita, viene controllato se il gioco è finito con il richiamo del metodo isGameOver() 
del GameController, che, a sua volta, richiama l'omonimo metodo di GameImpl. Se è così, allora 
viene chiamato il metodo loadGameOverWindow del GameController, che visualizza una finestra  
in cui c'è scritto "GAME OVER!" e che contiene un bottone che, una volta premuto, riporta al 
main menu; se, invece, viene chiusa tale finestra, allora l'applicazione si chiude.