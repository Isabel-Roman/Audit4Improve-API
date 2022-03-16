---
title: "Información de Audit4Improve"
permalink: /documentation/
---

# Audit4Improve-API

## Objetivo
Facilitar al programador de soluciones de auditoría la obtención de métricas desde diversas fuentes remotas y el cálculo de indicadores, para generar informes de evaluación de calidad

## Flexibilidad
- Deberá ofrecer la posibilidad de seleccionar la fuente de datos de la que se obtienen las métricas, proporcionando una interfaz única para la consulta de métricas
- Deberá ofrecer la posibilidad de elegir diferentes formas de persistir los informes, proporcionando una interfaz única independiente de la opción elegida
- Deberá ofrecer la posibilidad de elegir el formato de los informes, cuando éstos sean medios
- Deberá facilitar la selección de métricas e indicadores
- Deberá facilitar la creación de nuevas métricas e indicadores

## Dependencias
- Se requerirán clientes para la consulta de fuentes de métricas remotas, específicos a cada fuente
- Se requerirán clientes para la persistencia de informes, específicos para cada posiblidad de persistencia

## Trabajos actuales
La versión en desarrollo está centrada en la consulta a github y la persistencia en formato Excel
Para ello se utilizan:
- La API [github-API](https://github-api.kohsuke.org/)
- La API [Apache-POI](https://poi.apache.org/index.html)

