video-hls-converter
===================

A simple scala program that uses ffmpeg to convert videos to multivariant hls

Getting Started
---------------

Compile

    $ sbt compile

Test    

    $ sbt test

Run

    $ sbt run <in-dir> <out-dir> <bitrates>

Package (Create Jar)

    $ sbt one-jar

Run Jar:
    
    $ java -jar video-hls-converter.jar <in-dir> <out-dir> <bitrates>
    $ java -jar video-hls-converter.jar videos hls 128,256
