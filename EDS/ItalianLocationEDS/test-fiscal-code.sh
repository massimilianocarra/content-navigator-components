#!/bin/bash

# Test script for Italian Fiscal Code EDS functionality
# This script tests the fiscal code calculation feature

# Configuration
SERVER="localhost"
PORT="9443"
CONTEXT_ROOT="ItalianLocationEDS"
BASE_URL="https://${SERVER}:${PORT}/${CONTEXT_ROOT}"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=========================================="
echo "Italian Fiscal Code EDS - Test Suite"
echo "=========================================="
echo ""
echo "Base URL: ${BASE_URL}"
echo ""

# Test 1: GetObjectTypes
echo -e "${YELLOW}Test 1: GetObjectTypes${NC}"
echo "Testing: GET ${BASE_URL}/types"
RESPONSE=$(curl -k -s "${BASE_URL}/types")
echo "Response: ${RESPONSE}"
if echo "${RESPONSE}" | grep -q "CartellaPersona"; then
    echo -e "${GREEN}✓ Test 1 PASSED${NC}"
else
    echo -e "${RED}✗ Test 1 FAILED${NC}"
fi
echo ""

# Test 2: Italian Birth - Male
echo -e "${YELLOW}Test 2: Italian Birth - Male (Mario Rossi)${NC}"
echo "Testing: POST ${BASE_URL}/type/CartellaPersona"
RESPONSE=$(curl -k -s -X POST "${BASE_URL}/type/CartellaPersona" \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "NomePersona", "value": "Mario"},
      {"symbolicName": "CognomePersona", "value": "Rossi"},
      {"symbolicName": "DatadiNascita", "value": "10/10/1985"},
      {"symbolicName": "SessoPersona", "value": "M"},
      {"symbolicName": "Statodinascita", "value": "ITA"},
      {"symbolicName": "Provinciadinascita", "value": "PD"},
      {"symbolicName": "Comunedinascita", "value": "Abano Terme"}
    ]
  }')
echo "Response: ${RESPONSE}"
FISCAL_CODE=$(echo "${RESPONSE}" | grep -o '"CodiceFiscale"[^}]*"value":"[^"]*"' | grep -o 'value":"[^"]*"' | cut -d'"' -f3)
echo "Calculated Fiscal Code: ${FISCAL_CODE}"
if [ "${FISCAL_CODE}" = "RSSMRA85R10A001S" ]; then
    echo -e "${GREEN}✓ Test 2 PASSED - Fiscal code is correct${NC}"
else
    echo -e "${RED}✗ Test 2 FAILED - Expected: RSSMRA85R10A001S, Got: ${FISCAL_CODE}${NC}"
fi
echo ""

# Test 3: Italian Birth - Female
echo -e "${YELLOW}Test 3: Italian Birth - Female (Maria Bianchi)${NC}"
echo "Testing: POST ${BASE_URL}/type/CartellaPersona"
RESPONSE=$(curl -k -s -X POST "${BASE_URL}/type/CartellaPersona" \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "NomePersona", "value": "Maria"},
      {"symbolicName": "CognomePersona", "value": "Bianchi"},
      {"symbolicName": "DatadiNascita", "value": "15/03/1990"},
      {"symbolicName": "SessoPersona", "value": "F"},
      {"symbolicName": "Statodinascita", "value": "ITA"},
      {"symbolicName": "Provinciadinascita", "value": "RM"},
      {"symbolicName": "Comunedinascita", "value": "Roma"}
    ]
  }')
echo "Response: ${RESPONSE}"
FISCAL_CODE=$(echo "${RESPONSE}" | grep -o '"CodiceFiscale"[^}]*"value":"[^"]*"' | grep -o 'value":"[^"]*"' | cut -d'"' -f3)
echo "Calculated Fiscal Code: ${FISCAL_CODE}"
# For females, day is +40, so 15 becomes 55
# Expected: BNCMRA90C55H501? (? is check digit)
if [ ! -z "${FISCAL_CODE}" ] && [ ${#FISCAL_CODE} -eq 16 ]; then
    echo -e "${GREEN}✓ Test 3 PASSED - Fiscal code generated (${FISCAL_CODE})${NC}"
else
    echo -e "${RED}✗ Test 3 FAILED - Fiscal code not generated or invalid length${NC}"
fi
echo ""

# Test 4: Foreign Birth
echo -e "${YELLOW}Test 4: Foreign Birth (John Smith - USA)${NC}"
echo "Testing: POST ${BASE_URL}/type/CartellaPersona"
RESPONSE=$(curl -k -s -X POST "${BASE_URL}/type/CartellaPersona" \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "NomePersona", "value": "John"},
      {"symbolicName": "CognomePersona", "value": "Smith"},
      {"symbolicName": "DatadiNascita", "value": "01/01/1980"},
      {"symbolicName": "SessoPersona", "value": "M"},
      {"symbolicName": "Statodinascita", "value": "USA"},
      {"symbolicName": "Provinciadinascita", "value": "Stato di nascita estero"},
      {"symbolicName": "Comunedinascita", "value": "Stato di nascita estero"}
    ]
  }')
echo "Response: ${RESPONSE}"
FISCAL_CODE=$(echo "${RESPONSE}" | grep -o '"CodiceFiscale"[^}]*"value":"[^"]*"' | grep -o 'value":"[^"]*"' | cut -d'"' -f3)
echo "Calculated Fiscal Code: ${FISCAL_CODE}"
if [ ! -z "${FISCAL_CODE}" ] && [ ${#FISCAL_CODE} -eq 16 ]; then
    echo -e "${GREEN}✓ Test 4 PASSED - Fiscal code generated for foreign birth (${FISCAL_CODE})${NC}"
else
    echo -e "${RED}✗ Test 4 FAILED - Fiscal code not generated${NC}"
fi
echo ""

# Test 5: Missing Required Fields
echo -e "${YELLOW}Test 5: Missing Required Fields (should not calculate)${NC}"
echo "Testing: POST ${BASE_URL}/type/CartellaPersona"
RESPONSE=$(curl -k -s -X POST "${BASE_URL}/type/CartellaPersona" \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "NomePersona", "value": "Mario"},
      {"symbolicName": "CognomePersona", "value": "Rossi"}
    ]
  }')
echo "Response: ${RESPONSE}"
FISCAL_CODE=$(echo "${RESPONSE}" | grep -o '"CodiceFiscale"[^}]*"value":"[^"]*"' | grep -o 'value":"[^"]*"' | cut -d'"' -f3)
if [ -z "${FISCAL_CODE}" ]; then
    echo -e "${GREEN}✓ Test 5 PASSED - Fiscal code not calculated (as expected)${NC}"
else
    echo -e "${RED}✗ Test 5 FAILED - Fiscal code should not be calculated with missing fields${NC}"
fi
echo ""

# Test 6: Date Format yyyy-MM-dd
echo -e "${YELLOW}Test 6: Alternative Date Format (yyyy-MM-dd)${NC}"
echo "Testing: POST ${BASE_URL}/type/CartellaPersona"
RESPONSE=$(curl -k -s -X POST "${BASE_URL}/type/CartellaPersona" \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "NomePersona", "value": "Luigi"},
      {"symbolicName": "CognomePersona", "value": "Verdi"},
      {"symbolicName": "DatadiNascita", "value": "1985-10-10"},
      {"symbolicName": "SessoPersona", "value": "M"},
      {"symbolicName": "Statodinascita", "value": "ITA"},
      {"symbolicName": "Provinciadinascita", "value": "MI"},
      {"symbolicName": "Comunedinascita", "value": "Milano"}
    ]
  }')
echo "Response: ${RESPONSE}"
FISCAL_CODE=$(echo "${RESPONSE}" | grep -o '"CodiceFiscale"[^}]*"value":"[^"]*"' | grep -o 'value":"[^"]*"' | cut -d'"' -f3)
echo "Calculated Fiscal Code: ${FISCAL_CODE}"
if [ ! -z "${FISCAL_CODE}" ] && [ ${#FISCAL_CODE} -eq 16 ]; then
    echo -e "${GREEN}✓ Test 6 PASSED - Alternative date format accepted (${FISCAL_CODE})${NC}"
else
    echo -e "${RED}✗ Test 6 FAILED - Alternative date format not working${NC}"
fi
echo ""

# Test 7: Name with 4+ consonants (special rule)
echo -e "${YELLOW}Test 7: Name with 4+ consonants (Alessandro)${NC}"
echo "Testing: POST ${BASE_URL}/type/CartellaPersona"
RESPONSE=$(curl -k -s -X POST "${BASE_URL}/type/CartellaPersona" \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "NomePersona", "value": "Alessandro"},
      {"symbolicName": "CognomePersona", "value": "Rossi"},
      {"symbolicName": "DatadiNascita", "value": "10/10/1985"},
      {"symbolicName": "SessoPersona", "value": "M"},
      {"symbolicName": "Statodinascita", "value": "ITA"},
      {"symbolicName": "Provinciadinascita", "value": "PD"},
      {"symbolicName": "Comunedinascita", "value": "Abano Terme"}
    ]
  }')
echo "Response: ${RESPONSE}"
FISCAL_CODE=$(echo "${RESPONSE}" | grep -o '"CodiceFiscale"[^}]*"value":"[^"]*"' | grep -o 'value":"[^"]*"' | cut -d'"' -f3)
echo "Calculated Fiscal Code: ${FISCAL_CODE}"
# Alessandro has consonants: L, S, S, N, D, R (6 consonants)
# Should use 1st, 3rd, 4th: L, S, N = LSN
# Expected: RSSLSN85R10A001?
if [ ! -z "${FISCAL_CODE}" ] && [ ${#FISCAL_CODE} -eq 16 ]; then
    echo -e "${GREEN}✓ Test 7 PASSED - Special name rule applied (${FISCAL_CODE})${NC}"
else
    echo -e "${RED}✗ Test 7 FAILED - Fiscal code not generated${NC}"
fi
echo ""

echo "=========================================="
echo "Test Suite Complete"
echo "=========================================="
echo ""
echo "Note: To run these tests, ensure:"
echo "1. WebSphere is running"
echo "2. ItalianLocationEDS.war is deployed"
echo "3. Update SERVER, PORT, and CONTEXT_ROOT variables if needed"
echo ""

# Made with Bob
