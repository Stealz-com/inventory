@echo off
set SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/inventory_db?createDatabaseIfNotExist=true
mvn spring-boot:run
