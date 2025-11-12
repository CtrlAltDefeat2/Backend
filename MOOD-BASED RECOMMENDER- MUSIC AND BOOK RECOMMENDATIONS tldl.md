# Sinteză – Metodologie (Mood-Based Recommender System)

Studiul propune o metodologie clară, cu pași definiți, pentru implementarea unui **sistem de recomandare bazat pe starea de spirit (mood)**, care oferă utilizatorului sugestii de **muzică și cărți** personalizate.  

## 1. Analiza sentimentelor (Sentiment Analysis)
- Se folosește o tehnică de procesare a limbajului natural (NLP) pentru a analiza textul introdus de utilizator.  
- Scopul este de a deduce emoția exprimată în text — aceasta devine baza pentru identificarea stării de spirit curente.  

## 2. Detectarea stării de spirit (Mood Detection)
- Un model de învățare automată (machine learning) preia rezultatele analizei sentimentelor pentru a prezice starea de spirit exactă a utilizatorului.  
- Modelul a fost antrenat pe un **set de date cu emoții și sentimente variate**, permițând recomandări adaptate emoțional.  

## 3. Integrarea Spotify API
- API-ul Spotify este folosit pentru a căuta și furniza melodii potrivite dispoziției și preferințelor utilizatorului.  
- Această integrare permite **redarea directă a melodiilor** în cadrul aplicației, asigurând o experiență fluidă și personalizată.  

## 4. Calculul statisticilor agregate (Aggregate Statistics)
- Sistemul compară trăsături muzicale precum **danceability, energy, acousticness**, etc.  
- Pe baza acestor atribute, se identifică melodii similare cu cele preferate de utilizator sau potrivite cu dispoziția sa.  

## 5. Identificarea genului (Genre Identification)
- Se utilizează un **set de date ce leagă titlurile cărților de genurile lor literare**.  
- Acest pas permite sistemului să asocieze genuri de cărți cu starea de spirit a utilizatorului, oferind sugestii relevante (ex: tristețe → poezie, fericire → comedie).  

## 6. Integrarea Google Books API
- API-ul Google Books este folosit pentru a **prelua previzualizări ale cărților** din dataset.  
- Prin transmiterea titlului către API, sistemul poate afișa fragmente și detalii ale cărților potrivite dispoziției.  

## 7. Recomandările finale (Recommendations)
- **Spotify API** furnizează melodiile potrivite, iar **Google Books API** oferă cărțile relevante.  
- Rezultatele sunt afișate utilizatorului sub formă de **liste de recomandări muzicale și literare personalizate**.  

---

## Rezumat operațional
→ Utilizatorul introduce textul → se face analiza sentimentului → modelul ML detectează starea de spirit → sistemul interoghează Spotify și Google Books → sunt afișate recomandări de muzică și cărți potrivite cu starea emoțională.  

# Arhitectura Sistemului (System Architecture)

Sistemul oferă utilizatorului recomandări de **muzică și cărți** prin două modalități principale:

1. **Pe baza stării de spirit curente** – determinată din textul introdus de utilizator, pentru a oferi recomandări potrivite dispoziției sale.
2. **Pe baza preferințelor personale** – utilizatorul poate introduce direct numele unei melodii sau al unei cărți pentru a primi conținut similar.

Oricare dintre aceste opțiuni poate fi selectată de pe pagina principală a aplicației.

---

## 1. Analizorul de dispoziție (Mood Analyzer)

- Componenta de analiză a dispoziției este dezvoltată folosind un **model de Logistic Regression**.
- Setul de date utilizat conține **sentimente și etichete corespunzătoare de stare de spirit**, precum: *joy, fear, anger, sadness, disgust, shame, surprise*.
- Aceste date permit aplicarea tehnicilor de **învățare automată supravegheată** pentru analiza sentimentelor.
- Sistemul selectează starea de spirit dominantă (cu cel mai mare procent) pe baza textului introdus de utilizator.
- Această stare de spirit este transmisă unei funcții care selectează melodii corespunzătoare.

---

## 2. Selectarea melodiilor

- Se utilizează un **dataset muzical** care conține valori pentru atribute precum:
  - *danceability*, *acousticness*, *energy*, *instrumentalness*, *liveness*, *valence*, *loudness*, *speechiness*, *tempo*, împreună cu starea de spirit asociată.
- Pentru dispoziția selectată, se calculează valoarea medie a fiecărui atribut și se trimite cererea către **Spotify API**.
- Spotify returnează melodiile cu valori similare pentru aceste câmpuri, care sunt apoi filtrate.
- Cele mai potrivite melodii sunt recomandate utilizatorului.

---

## 3. Recomandări pe baza unei melodii specifice

- Dacă utilizatorul dorește melodii similare cu o anumită piesă:
  - Introduce numele piesei.
  - Sistemul obține **ID-ul melodiei** prin Spotify API.
  - Se extrag valorile atributelor piesei din modulul **Get Track Features**, ca obiect JSON.
  - Aceste valori sunt transmise către **endpoint-ul Get Recommendations** din Spotify API.
  - Spotify returnează melodii cu atribute similare, care sunt apoi afișate ca recomandări.

---

## 4. Recomandări de cărți bazate pe dispoziție

- Starea de spirit este determinată prin întrebări adresate utilizatorului.
- Se folosește un **set de date** care conține:
  - *titluri, autori, genuri și număr de pagini*.
- Utilizatorul oferă genul preferat și numărul maxim de pagini.
- Pe baza acestor criterii, sistemul selectează aleatoriu titluri din dataset.
- Titlurile selectate sunt transmise către **Google Books API** pentru a obține previzualizări și detalii despre autori.
- Rezultatele sunt afișate utilizatorului prin **framework-ul Streamlit**.

---

## 5. Recomandări pentru cărți similare

- Utilizatorul poate oferi titlul unei cărți specifice.
- Sistemul identifică **genul literar** al acesteia din dataset.
- Sunt selectate aleatoriu 5 cărți din același gen.
- Titlurile sunt trimise către **Google Books API** pentru a obține:
  - detalii despre autori,
  - linkuri de previzualizare,
  - imagini de copertă.
- Aceste informații sunt apoi afișate ca recomandări.


# Algoritmi (Algorithms)

Sistemul utilizează mai mulți algoritmi și tehnici de procesare a datelor pentru a realiza recomandările bazate pe dispoziția și preferințele utilizatorului.

---

## 1. Logistic Regression

- **Logistic Regression** este folosit ca algoritm de clasificare pentru a prezice starea de spirit a utilizatorului.  
- Modelul este antrenat pe un **set de date care conține sentimente și etichete corespunzătoare de mood**.  
- Astfel, sistemul poate determina cu acuratețe dispoziția utilizatorului pe baza analizei sentimentelor din textul introdus.  

---

## 2. Grouping and Aggregation

- Tehnica de **Grouping and Aggregation** utilizează funcția `groupby` din biblioteca **pandas** pentru a grupa datele în funcție de categoriile de mood.  
- Pentru fiecare grup (stare de spirit), se calculează **valoarea medie a fiecărei caracteristici** (feature).  
- Aceste valori medii sunt ulterior folosite pentru a corela trăsăturile melodiilor cu stările de spirit corespunzătoare, facilitând selecția recomandărilor muzicale.  

---

## 3. Content-Based Filtering

- **Content-based filtering** este utilizat pentru recomandările de cărți prin intermediul **Google Books API**.  
- Această metodă analizează **conținutul și atributele textuale** ale cărților (titlu, descriere, gen etc.) pentru a identifica elemente similare.  
- Pe baza acestor caracteristici, sistemul recomandă cărți care sunt relevante pentru preferințele utilizatorului sau pentru dispoziția sa curentă.  