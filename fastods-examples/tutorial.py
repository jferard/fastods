#!/usr/bin/python3
# coding: utf-8

#  FastODS - A very fast and lightweight (no dependency) library for creating ODS
#     (Open Document Spreadsheet, mainly for Calc) files in Java.
#     It's a Martin Schulz's SimpleODS fork
#     Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
#  SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
#     Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
#
#  This file is part of FastODS.
#
#  FastODS is free software: you can redistribute it and/or modify it under the
#  terms of the GNU General Public License as published by the Free Software
#  Foundation, either version 3 of the License, or (at your option) any later
#  version.
#
#  FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
#  WARRANTY; without even the implied warranty of MERCHANTABILITY or
#  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
#   for more details.
#
#  You should have received a copy of the GNU General Public License along with
#  this program. If not, see <http://www.gnu.org/licenses/>.

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
        table = {**{ord(c): None for c in "!,()"}, ord(' '): '-'}
        title_link = sline.lower().translate(table)
        return ("{} [{}](#{})".format('\t' * (c-1) +'*', sline, title_link))

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
