#!/usr/bin/python3
# coding: utf-8

#  FastODS - A very fast and lightweight (no dependency) library for creating ODS
#     (Open Document Spreadsheet, mainly for Calc) files in Java.
#     It's a Martin Schulz's SimpleODS fork
#     Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
"""
To create the FastODS tutorial.
"""

import os

BASE_DIR = "src/main/java/com/github/jferard/fastods/examples"
URL = "https://github.com/jferard/fastods/tree/master/fastods-examples"
IGNORE, COMMENT, CODE = 1, 2, 3


class ExamplesParser:
    def __init__(self, base_dir=BASE_DIR, url=URL, target_path="target/Tutorial.md"):
        self._base_url = url + "/" + base_dir
        self._base_dir = base_dir
        self._target_path = target_path
        self._state = IGNORE
        self._cur_url = None

    def create_tutorial(self):
        tes, lines = self._parse()
        self._write_tutorial(tes, lines)

    def _parse(self):
        self._lines = []
        self._tes = []
        for line in self._read_lines():
            sline = line.strip()
            if not sline:
                continue

            if self._state == IGNORE:
                self._parse_ignore(sline)
            elif self._state == COMMENT:
                self._parse_comment(line, sline)
            elif self._state == CODE:
                self._parse_code(line, sline)
            else:
                raise Exception("Unknown state")

        return self._tes, self._lines

    def _parse_ignore(self, sline):
        # just wait for the beginning of the tutorial
        if sline.startswith("// >> BEGIN TUTORIAL"):
            self._state = COMMENT

    def _parse_comment(self, line, sline):
        if sline.startswith("// << END TUTORIAL"):
            self._state = IGNORE
        elif sline.startswith("//"):  # continue COMMENT
            self._parse_comment_line(sline)
        else:  # a normal line => switch to CODE
            self._lines.append("")
            self._parse_code_line(line)
            self._state = CODE

    def _parse_code(self, line, sline):
        if sline.startswith("// << END TUTORIAL"):
            self._lines.append("")
            self._state = IGNORE
        elif sline.startswith("//"):  # a comment => switch to COMMENT
            self._lines.append("")
            self._parse_comment_line(sline)
            self._state = COMMENT
        else:  # continue CODE
            self._parse_code_line(line)

    def _parse_comment_line(self, sline):
        if len(sline) > 3:  # maybe a TOC entry
            line_content = sline[3:]
            te = self._toc_entry(line_content)
            if te is not None:  # a new TOC entry
                print("  Append TOC entry: " + te)
                self._tes.append(te)
                self._lines.append(line_content.strip())
                if self._cur_url is not None:
                    self._lines.append(
                        "*The source code for this section is available [here]({}).*\n".format(
                            self._cur_url))
                    self._cur_url = None
            else:
                self._lines.append(line_content.strip())
        else:  # ignore line
            self._lines.append("")

    def _parse_code_line(self, line):
        self._lines.append(line.rstrip())

    def _write_tutorial(self, tes, lines):
        with open(self._target_path, "w", encoding="utf8") as d:
            for line in lines:
                if line == "[TOC]":
                    d.write("# Table of Contents\n")
                    for te in tes:
                        d.write(te + "\n")
                else:
                    d.write(line + "\n")

    def _read_lines(self):
        for root, dirs, fnames in os.walk(self._base_dir):
            fnames = sorted(fnames, key=lambda fname: fname if "_" in fname else "0")
            for fname in fnames:
                fullname = os.path.join(root, fname)
                self._cur_url = self._base_url + "/" + fname
                with open(fullname, "r", encoding="utf-8") as s:
                    print("Parse: " + fullname)
                    yield from s

    @staticmethod
    def _toc_entry(sline):
        sline = sline.strip()
        if sline.startswith('#'):
            c = sline.count('#')
            sline = sline.lstrip('#').strip()
            table = {**{ord(c): None for c in "!,()"}, ord(' '): '-'}
            title_link = sline.lower().translate(table)
            return ("{} [{}](#{})".format('\t' * (c - 1) + '*', sline, title_link))


if __name__ == "__main__":
    ExamplesParser().create_tutorial()
