#!/bin/bash

# Script per testare il servlet EDS manualmente

# Configurazione
ICN_URL="https://rocky.example.com:20002/navigator"
PLUGIN_ID="ItalianLocationEDSPlugin"
SERVLET_PATH="UpdateObjectTypeServlet"
OBJECT_TYPE="CartellaPersona"

# URL completo del servlet
SERVLET_URL="${ICN_URL}/plugin/${PLUGIN_ID}/${SERVLET_PATH}/${OBJECT_TYPE}"

echo "=========================================="
echo "Test Italian Location EDS Servlet"
echo "=========================================="
echo ""
echo "URL: ${SERVLET_URL}"
echo ""

# Test 1: Richiesta iniziale (caricamento province)
echo "Test 1: Caricamento iniziale province"
echo "--------------------------------------"

curl -k -X POST "${SERVLET_URL}" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "objectId": "test123",
    "requestMode": "initialNewObject",
    "properties": [
      {
        "symbolicName": "Provinciadinascita",
        "value": ""
      },
      {
        "symbolicName": "Comunedinascita",
        "value": ""
      }
    ],
    "clientContext": {
      "userId": "admin",
      "locale": "it-IT",
      "desktop": "admin"
    }
  }' \
  -w "\n\nHTTP Status: %{http_code}\n" \
  -v

echo ""
echo ""

# Test 2: Richiesta con provincia selezionata (caricamento comuni)
echo "Test 2: Caricamento comuni per provincia MI"
echo "--------------------------------------"

curl -k -X POST "${SERVLET_URL}" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "objectId": "test123",
    "requestMode": "inProgressChanges",
    "properties": [
      {
        "symbolicName": "Provinciadinascita",
        "value": "MI"
      },
      {
        "symbolicName": "Comunedinascita",
        "value": ""
      }
    ],
    "clientContext": {
      "userId": "admin",
      "locale": "it-IT",
      "desktop": "admin"
    }
  }' \
  -w "\n\nHTTP Status: %{http_code}\n" \
  -v

echo ""
echo "=========================================="
echo "Test completato"
echo "=========================================="

# Made with Bob
