#!/bin/sh
rm -rf dist/
mkdir dist

mvn clean install --batch-mode -DskipTests -P deliflow.oauth.login
cp target/deliflow-oauth-*.jar dist/
cp target/deliflow-oauth-*.jar ../../gocd/server/plugins/external/


mvn clean install --batch-mode -DskipTests -P deliflow.cas-oauth2.login
cp target/deliflow-oauth-*.jar dist/
cp target/deliflow-oauth-*.jar ../../gocd/server/plugins/external/
