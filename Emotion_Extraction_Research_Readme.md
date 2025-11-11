
# README – Research on Emotion Extraction from Audio and Text

## 1. Overview

This research explores methods for **emotion extraction from music and lyrics** to enable cross-modal recommendations — for example, using a Spotify playlist to recommend books or movies that share a similar emotional profile.

The goal is to represent the **affective signature** of songs in a numerical format that can later be compared with embeddings of textual media (books, summaries, movie descriptions, etc.).

The research focuses on two modalities:
1. **Audio** – extracting emotional features from the sound itself.
2. **Text** – analyzing the emotional meaning of lyrics.

---

## 2. Emotion Extraction from Audio

### 2.1 Concept

Emotion recognition from audio aims to map the sound waveform into **affective dimensions** or **discrete emotion categories**.

Two main modeling approaches:
- **Continuous affective dimensions:**  
  - **Valence:** from negative (sad) to positive (happy)  
  - **Arousal:** from calm to energetic  
  - **Dominance:** from weak/submissive to strong/confident  

- **Discrete emotion classes:**  
  *happy, sad, angry, calm, fearful, neutral, etc.*

---

### 2.2 Pretrained Model Approaches

#### a. `superb/wav2vec2-base-superb-er`
- Transformer-based model fine-tuned for **speech emotion recognition**.  
- Outputs discrete emotion categories with probabilities.  

#### b. `audeering/wav2vec2-large-robust-12-ft-emotion-msp-dim`
- Outputs continuous **valence, arousal, dominance** scores.  
- More expressive, but occasionally version-sensitive in Colab.  

---

### 2.3 Using DEAM Dataset for Local Emotion Mapping

#### a. About the Dataset

**DEAM (Database for Emotional Analysis in Music)** is one of the most widely used public datasets for **music emotion recognition**.

- Annotated continuously with **valence** and **arousal**
- Metadata (genre, ID, duration)
- [Official site](https://cvml.unige.ch/databases/DEAM/)

#### b. Integration in Research

1. **Model Training**  
   Train a local model mapping extracted audio features to valence/arousal scores.  
   Tools: `librosa`, `openL3`, `VGGish`

2. **Emotion Mapping (Valence–Arousal → Discrete Labels)**  
   Map continuous valence/arousal values to categorical emotions:  

   | Valence | Arousal | Mapped Emotion |
   |----------|----------|----------------|
   | high | high | Happy / Excited |
   | high | low | Relaxed / Content |
   | low | high | Angry / Tense |
   | low | low | Sad / Depressed |

3. **Validation**  
   Evaluate pretrained models against DEAM annotations using correlation metrics.

4. **Cross-Modal Alignment**  
   Combine DEAM-based emotion vectors with text embeddings for unified emotion space.

---

## 3. Emotion Extraction from Text (Lyrics)

### 3.1 Concept

Lyrics analysis detects emotional tone expressed by words, complementing the audio-based emotion.

### 3.2 Methods

#### a. Lexicon-Based (NRCLex)

Uses predefined word–emotion mappings from NRC Emotion Lexicon.

#### b. Sentiment Analysis

Detects polarity (positive, negative, neutral) using models like `TextBlob` or `cardiffnlp/twitter-roberta-base-sentiment`.

#### c. Transformer-Based Models

Contextual emotion detection using models like `j-hartmann/emotion-english-distilroberta-base`.

---

## 4. Pipeline Overview

The pipeline for extracting and integrating emotional information across modalities is as follows:

1. **Input Data**  
   - Spotify playlist → list of songs with metadata and lyrics.

2. **Audio Processing**  
   - Extract features using a pretrained model or DEAM-trained model.  
   - Output: `[valence, arousal, dominance]` or categorical emotion.

3. **Text Processing**  
   - Extract lyrics.  
   - Analyze using NRCLex or a transformer-based model.  
   - Output: emotion distribution over {joy, sadness, anger, etc.}.

4. **Fusion Layer**  
   - Combine audio and text emotion vectors (weighted average or concatenation).

5. **Emotion Representation**  
   - Generate a single emotion vector for the entire song or playlist.

6. **Cross-Modal Matching**  
   - Compare playlist emotion vectors to embeddings of books, movies, or other media.  
   - Use cosine similarity to recommend emotionally aligned content.

---