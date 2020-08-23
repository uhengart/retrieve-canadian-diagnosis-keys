# retrieve-canadian-diagnosis-keys
Perl script for retrieving the diagnosis keys (aka temporary exposure keys) uploaded by the [Canadian COVID Alert app](https://github.com/cds-snc/covid-alert-app).

Run the script and it will create a directory whose name is the current time (in UTC) containing the following files:
* *config.json* containing the current configuration for COVID Alert.
* *all.zip* containing all currently available diagnosis keys.
* 14 files containing all currently available diagnosis keys with the name of the file indicating the day when the keys in the file were uploaded.

You can use the [diagnosis-keys tools](https://github.com/mh-/diagnosis-keys) for analyzing the downloaded diagnosis keys.

