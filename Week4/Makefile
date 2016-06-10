FILES=Board.java Solver.java

all:$(FILES)
	javac-algs4 $(FILES)

zip:$(FILES)
	zip 8puzzle.zip $(FILES)

clean:
	rm *.class *.zip

add:
	git add $(FILES) Makefile Readme.md
