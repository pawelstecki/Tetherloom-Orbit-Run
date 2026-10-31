# R8 rules for Tetherloom: Orbit Run.
#
# Activities and the custom GameView are referenced from AndroidManifest.xml and
# layout XML, so AAPT already generates keep rules for them. The game does not use
# reflection or serialization, so no extra keep rules are needed.
#
# Keep line numbers so crash reports in Play Console stay readable after the
# mapping.txt file is uploaded (AGP uploads it automatically with the AAB).
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
