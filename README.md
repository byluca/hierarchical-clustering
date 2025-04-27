# 📊 Dendrogramma Client-Server Application

## 🚀 Introduction
The Dendrogramma Client-Server Application is a Java-based tool that provides a graphical interface for connecting to a server, loading data, learning hierarchical clusters, and visualizing dendrograms. It is designed for data analysis and hierarchical clustering visualization.

## ✨ Features
- 🔌 Server connection with configurable IP and port settings
- 💾 Database integration for loading data from tables
- 🌳 Dendrogram generation with adjustable depth and distance metrics
- 📁 File operations for saving and loading dendrograms
- 📏 Support for single-link and average-link distance methods

## 📖 Usage Instructions
1. 🔌 Connect to Server: Enter server IP and port, then click "Connetti"
2. 📊 Load Data: Enter the table name and click "Carica Dati"
3. 🧠 Generate Dendrogram: Set parameters and click "Apprendi Dendrogramma"
4. 💾 Save Results: Use "Salva Dendrogramma" to export your dendrogram
5. 📂 Alternative: Load existing dendrograms using "Carica Dendrogramma"

## ⚙️ Prerequisites
- ☕ Java Runtime Environment (JRE) 8 or later
- 🖥️ Access to a compatible dendrogramma server
- 📚 Sufficient memory for processing datasets

## 🏗️ Project Structure
- 🖼️ ClientGUI.java: Main graphical interface
- ⌨️ Keyboard.java: Utility class for input handling

## 🔧 Technical Details
The application utilizes:
- 🎨 Swing GUI Framework for the user interface
- 🔄 Socket Communication for client-server interaction
- 📦 Object Serialization for data exchange

## 📜 License
This project is licensed under the MIT License. See the LICENSE file for details.
