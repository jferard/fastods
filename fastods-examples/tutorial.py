#!/usr/bin/python3
# coding: utf-8
import os

def read_lines():
    for root, dirs, fnames in os.walk("src/main/java/com/github/jferard/fastods/examples"):
        fnames = sorted(fnames, key=lambda fname: fname if "_" in fname else "0")
        for fname in fnames:
            fullname = os.path.join(root, fname)
            # print (fullname)
            with open(fullname, "r", encoding="utf-8") as s:
                yield from s

IGNORE, COMMENT, CODE = range(1,4)

state = IGNORE


def toc_entry(sline):
    sline = sline.strip()
    if sline.startswith('#'):
        c = sline.count('#')
        sline = sline.lstrip('#').strip()
        return ("{} [{}](#{})".format('\t'*(c-1)+'*', sline, sline.lower().translate({ord('!'): None, ord(','):None, ord(' '):'-'})))

tes = []
lines = []

for line in read_lines():
    sline = line.strip()
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
                te = toc_entry(sline[3:])
                if te is not None:
                    tes.append(te)
                lines.append(sline[3:].strip())
            else:
                lines.append("")
        else:
            lines.append("")
            lines.append(line.rstrip())
            state = CODE
    elif state == CODE:
        if sline.startswith("// << END TUTORIAL"):
            lines.append("")
            state = IGNORE
        elif sline.startswith("//"):
            lines.append("")
            if len(sline) > 2:
                te = toc_entry(sline[3:])
                if te is not None:
                    tes.append(te)
                lines.append(sline[3:].strip())
            else:
                lines.append("")
            state = COMMENT
        else:
            lines.append(line.rstrip())
    else:
        raise Exception("Unknown state")

with open("target/Tutorial.md", "w", encoding="utf8") as d:
    for line in lines:
        if line == "[TOC]":
            d.write("# Table of Contents\n")
            for te in tes:
                d.write(te + "\n")
        else:
            d.write(line + "\n")
