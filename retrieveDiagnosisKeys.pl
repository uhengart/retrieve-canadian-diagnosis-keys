#!/usr/bin/perl

# script for downloading all currently available COVID Alert diagnosis keys 

use POSIX;
use Digest::SHA qw(hmac_sha256_hex);

sub determineDiagnosisKeysURL {

    my @params = @_;

    # determine URL for retrieving diagnosis keys as outlined here:
    # https://github.com/cds-snc/covid-alert-server/tree/master/proto#retrieveregiondatenumberhmac

    my $MCC_CODE = "302";

    # retrieved from COVID Alert APK   
    my $retrieveUrl = "https://retrieval.covid-notification.alpha.canada.ca";
    my $hmacKey = "3631313045444b345742464633504e44524a3457494855505639593136464a3846584d4c59334d30";

    # See https://github.com/cds-snc/covid-shield-server/pull/176
    my $LAST_14_DAYS_PERIOD = "00000";

    my $HOURS_PER_PERIOD = 24;

    if ($#params >= 0) {
        # URL part for retrieving all keys that were submitted the given number of days ago 
        $daysAgo = $params[0];
        $periodStr = floor(time / 3600 / $HOURS_PER_PERIOD) - $daysAgo;
    }
    else {
        # URL part for retrieving all available keys
        $periodStr = $LAST_14_DAYS_PERIOD;
    }

    my $now = floor(time / 3600);
    my $message = $MCC_CODE.":".$periodStr .":".$now;
    my $hmac=hmac_sha256_hex($message, pack('H*', $hmacKey));
    my $url = $retrieveUrl."/retrieve/".$MCC_CODE."/".$periodStr."/".$hmac;
    #print $url;

    return $url;
}

$now = time;
$dateString = strftime "%Y-%m-%d_%H:%M:%S_UTC", gmtime($now);
mkdir $dateString;

# retrieve configuration 
$url = "https://retrieval.covid-notification.alpha.canada.ca/exposure-configuration/CA.json";
$output = $dateString."/config.json";
$cmd = "wget -q -O ".$output." ".$url;
#print $cmd;
system $cmd;

# retrieve all available keys
$url = determineDiagnosisKeysURL();
$output = $dateString."/all.zip";
$cmd = "wget -q -O ".$output." ".$url;
#print $cmd;
system($cmd);

# retrieve all available keys based on the day they were uploaded
$days = 14;
for($i=0; $i<$days; $i++) {

    $file = strftime "%Y-%m-%d", gmtime($now);
    $url = determineDiagnosisKeysURL($i);
    $output = $dateString."/".$file.".zip";
    $cmd = "wget -q -O ".$output." ".$url;
    #print $cmd;
    system($cmd);

    $now -= 86400;
}
