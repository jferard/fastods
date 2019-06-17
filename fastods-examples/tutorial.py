#!/usr/bin/python3
# coding: utf-8
import os

def read_lines():
    for root, dirs, fnames in os.walk("src/main/java/com/github/jferard/fastods/examples"):
        fnames = sorted(fnames, key=lambda fname: fname if "_" in fname else "0")
        for fname in fnames:
            fullname = os.path.join(root, fname)
            print (fullname)
            with open(fullname, "r", encoding="utf-8") as s:
                yield from s

IGNORE, COMMENT, CODE = range(1,4)

state = IGNORE

with open("target/Tutorial.md", "w", encoding="utf8") as d:
    for line in read_lines():
        sline = line.lstrip()
        if not sline:
            continue

        if state == IGNORE:
            if sline.startswith("// >> BEGIN TUTORIAL"):
                state = COMMENT
        elif state == COMMENT:
            if sline.startswith("// << END TUTORIAL"):
                state = IGNORE
            elif sline.startswith("//"):
                if len(sline) > 3:
                    d.write(sline[3:])
                else:
                    d.write("\n")
            else:
                d.write("\n")
                d.write(line)
                state = CODE
        elif state == CODE:
            if sline.startswith("// << END TUTORIAL"):
                state = IGNORE
            elif sline.startswith("//"):
                d.write("\n")
                if len(sline) > 2:
                    d.write(sline[3:])
                else:
                    d.write("\n")
                state = COMMENT
            else:
                d.write(line)
        else:
            raise Exception("Unknown state")
