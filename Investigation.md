# AI Research

## Overview

Proiectul propune un sistem de recomandare bazat pe inteligenta artificiala care face legatura intre **muzica** si **carti**.  
Aplicatia primeste ca input un **playlist de Spotify** si returneaza o lista de **carti recomandate**, alese pe baza asemanarii emotionale si semantice dintre playlist si descrierile cartilor.

---

## Obiectivul AI

- Extrage **caracteristici emotionale si semantice** din playlisturi de muzica.
- Extrage **caracteristici tematice si emotionale** din descrieri de carti.
- Proiecteaza ambele tipuri de date intr-un **spatiu latent comun**, astfel incat playlisturile si cartile similare sa fie apropiate vectorial.
- Recomanda carti prin cautare de similaritate (cosine similarity) intre embedding-ul playlistului si embedding-urile cartilor.

---

## 1. Reprezentarea datelor

### 1.1. Carti

#### Feature-uri extrase

1. **Vector semantic (text embedding)**

   - Model: `text-embedding-3-large` (1536D) sau `all-mpnet-base-v2` (768D)
   - Scop: captarea semnificatiei tematice a descrierii

2. **Vector emotional (emotion embedding)**

   - Model: `j-hartmann/emotion-english-distilroberta-base`
   - Output: distributie de probabilitati pe 6 emotii (joy, sadness, fear, anger, surprise, disgust)

3. **Vector final carte**
   v_book = [E_semantic, E_emotional]
   Dimensiune totala: ~774D (reducere la 256–512D prin PCA sau autoencoder)

---

### 1.2. Muzica

#### Feature-uri audio (Spotify)

- `valence`, `energy`, `danceability`, `acousticness`, `speechiness`, `tempo`, `instrumentalness`, `liveness`
- ~8–10 dimensiuni

#### Embedding semantic (optional)

- Descriere textuala generata automat:
  > “An energetic and romantic pop song with a hopeful mood.”
- Embedding obtinut cu acelasi model ca pentru carti (768D)

#### Vector final melodie

v_music = [E_audio, E_semantic_music]

#### Vector playlist

Agregare:
\[
v*{playlist} = \frac{1}{N} \sum*{i=1}^{N} v\_{music_i}
\]
Ponderare optionala in functie de popularitate, energy sau valence.

---

## 2. Normalizare

### a) Scaling numeric

Pentru feature-uri audio:

- **Min-Max scaling** → interval [0, 1]
- sau **Z-score normalization** daca distributia e normala

### b) Normalizare vectoriala

Toti vectorii embedding (muzica si carti) se normalizeaza pe **norma L2**:
\[
\hat{v} = \frac{v}{||v||\_2}
\]
Aceasta permite folosirea **cosine similarity** drept metrica principala.

---

## 3. Metrici de Similaritate

| Metrica                | Descriere                      | Avantaj                             |
| ---------------------- | ------------------------------ | ----------------------------------- |
| **Cosine Similarity**  | Masoara unghiul dintre vectori | Standard pentru embeddings textuale |
| **Dot Product**        | Eficient dupa L2-normalizare   | Echivalent cu cosine                |
| **Euclidean Distance** | Distanta geometrica bruta      | Mai putin relevanta semantic        |

---

## 4. Reducerea Dimensionalitatii

Dupa concatenare, vectorii pot fi mari (>1000D).  
Pentru eficienta se aplica:

- **PCA (Principal Component Analysis)** – liniar, rapid
- **Autoencoder** – nelinear, invata proiectii mai complexe
- **Dimensiune finala tipica:** 256–512D

---

## 5. Stocarea Embedding-urilor

### a) Vector Databases

Sunt baze de date specializate pentru stocarea si interogarea vectorilor prin similaritate.

| Solutie                        | Tip                   | Descriere                                       |
| ------------------------------ | --------------------- | ----------------------------------------------- |
| **FAISS**                      | Open-source (Meta AI) | Rapid, local, suporta miliarde de vectori       |
| **Pinecone**                   | Cloud                 | Serviciu scalabil cu API simplu                 |
| **Weaviate / Milvus / Chroma** | Open-source / SaaS    | Suport pentru metadate si embedding-uri hibride |

### b) Index FAISS

- Tip: `IndexFlatIP` (Inner Product = Cosine)
- Dimensiune: 256–512
- Posibil clustering: `IVF` sau `HNSW` pentru date mari

---

## 6. Modele si Dimensiuni Recomandate

| Componenta           | Model                                     | Dimensiune | Normalizare | Metrica    |
| -------------------- | ----------------------------------------- | ---------- | ----------- | ---------- |
| Embedding text carte | `sentence-transformers/all-mpnet-base-v2` | 768        | L2          | Cosine     |
| Emotii carte         | `emotion-roberta-base`                    | 6          | L2          | –          |
| Audio playlist       | Spotify features                          | 8          | Min-Max     | –          |
| Spatiu comun         | PCA / autoencoder                         | 256–512    | L2          | Cosine     |
| Vector DB            | FAISS / Pinecone                          | 256–512    | –           | ANN Cosine |

---

## 7. Posibile Directii de Training

### **A. Contrastive Learning (CLIP-like)**

Antrenarea unui model multimodal care invata sa apropie embedding-urile muzicii si cartilor similare:
\[
L = - \log \frac{\exp(\text{sim}(v*{music}, v*{book}) / \tau)}{\sum*j \exp(\text{sim}(v*{music}, v\_{book_j}) / \tau)}
\]

- Modelul invata maparea intre domenii.
- Necesita perechi (muzica–carte) etichetate.

### **B. Autoencoder Alignment**

- Doua encodere (pentru muzica si text) care proiecteaza in acelasi spatiu latent.
- Loss: minimizarea distantei cosinus intre perechi potrivite.

### **C. Emotion Alignment**

- Ambii encoderi (muzica si carte) invata sa mapeze in acelasi spatiu emotional (6–10D).
- Avantaj: interpretabilitate crescuta (“aceeasi tristete, speranta sau energie”).

---

## 8. Fluxul AI (Conceptual Pipeline)

1. **Preprocesare date**
   - Curatare, extragere descrieri, feature-uri numerice
2. **Vectorizare**
   - Generare embedding semantic si emotional pentru carti
3. **Stocare vectori**
   - in baza de date vectoriala
4. **Agregare playlist**
   - Generare embedding audio si semantic pentru melodii
   - Media vectorilor melodiilor
5. **Normalizare + Reducere**
   - PCA / Autoencoder
6. **Cautare similaritate**
   - Cosine similarity intre `v_playlist` si `v_book`
7. **Recomandare**
   - Top-N carti cu scoruri de similaritate maxime

## 9. Resurse

- [Mood-Based Recommender: Music And Book Recommendations](https://ijnrd.org/viewpaperforall.php?paper=IJNRD2405079)
- [The Multimodal Evolution of Vector Embeddings](https://www.twelvelabs.io/blog/multimodal-embeddings)

---
