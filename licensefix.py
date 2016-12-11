# -*- coding: utf-8 -*-
# Name: License fix
# Copyright:   Copyright (C) 2016 J. Férard <https://github.com/jferard>
# Licence:     GPL v3
#-------------------------------------------------------------------------------
import os

LICENSE = """/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * ****************************************************************************/
"""

def main():
    for dirpath, dirname, filenames in os.walk("src"):
        for filename in filenames:
            if filename.endswith(".java"):
                fixlicense(os.path.join(dirpath, filename))

def fixlicense(filename):
    print (filename)
    output = []
    with open(filename, 'r', encoding='utf-8') as f:
        handle_line = gobble_opt_blanks
        for line in f:
            next_handle_line = handle_line(output, line)
            while next_handle_line is not None:
                handle_line = next_handle_line
                next_handle_line = handle_line(output, line)

    with open(filename, 'w', encoding='utf-8') as f:
        for line in output:
            f.write(line)

def gobble_opt_blanks(output, line):
    if line.strip():
        return handle_opt_begin_comment
    else:
        return None

def handle_opt_begin_comment(output, line):
    if line.strip().startswith("/*"):
        if line.strip().endswith("*/"):
            return gobble_last_comment_line
        else:
            return handle_comment
    else:
        return handle_print_header

def gobble_last_comment_line(output, line):
    if line.strip().endswith("*/"):
        return None
    else:
        return gobble_opt_blanks


def handle_comment(output, line):
    if line.strip().endswith("*/"):
        return gobble_last_comment_line
    else:
        return None

def handle_print_header(output, line):
    output.append(LICENSE)
    return handle_print_line

def handle_print_line(output, line):
    output.append(line) # first non blank line
    return None

if __name__ == '__main__':
    main()
