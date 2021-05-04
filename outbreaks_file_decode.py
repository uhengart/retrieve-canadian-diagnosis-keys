#!/usr/bin/python3

import sys
import outbreaks_pb2
import zipfile
import datetime

def get_string_from_datetime(date_time):
    return date_time.strftime('%Y-%m-%d %H:%M:%S %Z')

zip_file = zipfile.ZipFile("outbreaks.2021-05-04.zip")
export_bin = zip_file.read("export.bin")

# The binary format file consists of a 16 byte header,
# containing "EK Export v1​" right padded with whitespaces in UTF-8
label = "EK Export v1"
header_str = label + ' ' * (16-len(label))
header = header_str.encode("UTF-8")

if not export_bin[:len(header)] == header:
    print("ERROR: export.bin (extracted from %s) does not start with '%s'" % (zip_file, header_str))
    print("ERROR: instead '%s'" % str(export_bin[:len(header)]))
    exit(1)

g = outbreaks_pb2.OutbreakEventExport()
try: 
    g.ParseFromString(export_bin[len(header):])
except:
    sys.exit(-1)
        
print("file timestamps: start "+get_string_from_datetime(g.start_timestamp)+", end "+get_string_from_datetime(g.end_timestamp))
printf("number of locations: " + str(len(g.locations)))
for location in g.locations:
    print("location_id: " + location.location_id + ", start_time: " + get_string_from_datetime(location.start_time.ToDatetime()) + ", end_time: " + get_string_from_datetime(location.end_time.ToDatetime))

