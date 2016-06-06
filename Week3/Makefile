FILES=Point.java LineSegment.java BruteCollinearPoints.java FastCollinearPoints.java

all:$(FILES)
	javac-algs4 $(FILES)

zip:$(FILES)
	zip collinear.zip $(FILES)

clean:
	rm *.class *.zip

add:
	git add $(FILES) Makefile
