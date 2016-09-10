#!/bin/sh
rm -rf dist/
mkdir dist

mvn clean install --batch-mode -DskipTests -P deliflow.oauth.login
cp target/deliflow-oauth-*.jar dist/
cp target/deliflow-oauth-*.jar ../ref/gocd/server/plugins/external/