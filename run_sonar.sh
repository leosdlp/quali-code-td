#!/bin/bash

set -e

if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

if [ -z "$SONAR_TOKEN" ]; then
  echo "Erreur : SONAR_TOKEN est manquant dans le fichier .env"
  exit 1
fi

echo "Lancement des tests Maven..."
./mvnw clean verify

echo "Lancement de l'analyse Sonar..."
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.projectKey=f1-quality-gate \
  -Dsonar.projectName="F1 Quality Gate" \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token="$SONAR_TOKEN" \
  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
  -Dsonar.coverage.exclusions="**/model/entity/**,**/*Application.java"

echo ""
echo "Sonar dashboard:"
grep "dashboardUrl" target/sonar/report-task.txt | cut -d'=' -f2-