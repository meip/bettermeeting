[![Stories in Ready](https://badge.waffle.io/meip/bettermeeting.png?label=ready&title=Ready)](https://waffle.io/meip/bettermeeting)
[![Build Status](https://travis-ci.org/meip/bettermeeting.svg)](https://travis-ci.org/meip/bettermeeting)
# Verbesserung von Meetingnotizen durch Gamification (HSR SA)

## Intro

### Live Demo on Heroku
The bettermeeting App is deployed on heroku.com
[Bettermeeting Demo](https://bettermeeting.herokuapp.com/)

## Dev-Setup Getting Started

### Dev Mode

* Load dependencies via `./activator update`
* Run via `./activator ~run`
* Go to [localhost:9000](http://localhost:9000)

This uses the normal JavaScript files and loads libraries from the downloaded WebJars.
Use `./activator-debug ~run` instead of normal activator-command to run in remote debug configuration

### Prod Mode

Running:

* Run `./activator start -Dconfig.resource=prod.conf`

Deployment:

* Produce executable via `./activator clean dist`
* Extract `unzip target/universal/bettermeeting-0.x.x.zip`
This uses the uglified JavaScript files, versioned and compressed assets, and loads WebJars resources from the jsDelivr CDN.
