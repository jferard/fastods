/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.attribute;

/**
 * https://docs.libreoffice.org/xmloff/html/EventMap_8cxx_source.html
 * <p>
 * for DOM events:
 * 19.429script:event-name
 * https://www.w3.org/TR/2000/REC-DOM-Level-2-Events-20001113/events
 * .html#Events-eventgroupings-htmlevents
 * <p>
 * for form events:
 * 13.6Event Listeners
 */
public enum ScriptEvent implements AttributeValue {
    // DOM events
    /**
     * Event: 'on-select'
     */
    ON_SELECT("dom", "select"),


    /**
     * Event: 'on-resize'
     */
    ON_RESIZE("dom", "resize"),

    /**
     * Event: 'on-mouse-over'
     */
    ON_MOUSE_OVER("dom", "mouseover"),


    /**
     * Event: 'on-click'
     */
    ON_CLICK("dom", "click"),


    /**
     * Event: 'on-mouse-out'
     */
    ON_MOUSE_OUT("dom", "mouseout"),

    /**
     * Event: 'on-load'
     */
    ON_LOAD("dom", "load"),


    /**
     * Event: 'on-unload'
     */
    ON_UNLOAD("dom", "unload"),


    /**
     * Event: 'on-focus'
     */
    ON_FOCUS("dom", "DOMFocusIn"),

    /**
     * Event: 'on-unfocus'
     */
    ON_UNFOCUS("dom", "DOMFocusOut"),


    /**
     * Event: 'on-error'
     */
    ON_ERROR("dom", "error"),


    /**
     * Event: 'on-change'
     */
    ON_CHANGE("dom", "change"),


    /**
     * Event: 'on-keydown'
     */
    ON_KEYDOWN("dom", "keydown"),

    /**
     * Event: 'on-keyup'
     */
    ON_KEYUP("dom", "keyup"),


    /**
     * Event: 'on-mousemove'
     */
    ON_MOUSEMOVE("dom", "mousemove"),


    /**
     * Event: 'on-mousedown'
     */
    ON_MOUSEDOWN("dom", "mousedown"),


    /**
     * Event: 'on-mouseup'
     */
    ON_MOUSEUP("dom", "mouseup"),

    /**
     * Event: 'on-reset'
     */
    ON_RESET("dom", "reset"),


    /**
     * Event: 'on-submit'
     */
    ON_SUBMIT("dom", "submit"),

    /**
     * Event: 'on-mouseover'
     */
    ON_MOUSEOVER("dom", "mouseover"),


    /**
     * Event: 'on-mouseout'
     */
    ON_MOUSEOUT("dom", "mouseout"),


    /**
     * Event: 'on-blur'
     */
    ON_BLUR("dom", "DOMFocusOut"),


    // Office events
    /**
     * Event: 'on-insert-start'
     */
    ON_INSERT_START("office", "insert-start"),


    /**
     * Event: 'on-insert-done'
     */
    ON_INSERT_DONE("office", "insert-done"),


    /**
     * Event: 'on-mail-merge'
     */
    ON_MAIL_MERGE("office", "mail-merge"),


    /**
     * Event: 'on-alpha-char-input'
     */
    ON_ALPHA_CHAR_INPUT("office", "alpha-char-input"),


    /**
     * Event: 'on-non-alpha-char-input'
     */
    ON_NON_ALPHA_CHAR_INPUT("office", "non-alpha-char-input"),

    /**
     * Event: 'on-move'
     */
    ON_MOVE("office", "move"),


    /**
     * Event: 'on-page-count-change'
     */
    ON_PAGE_COUNT_CHANGE("office", "page-count-change"),


    /**
     * Event: 'on-load-error'
     */
    ON_LOAD_ERROR("office", "load-error"),


    /**
     * Event: 'on-load-cancel'
     */
    ON_LOAD_CANCEL("office", "load-cancel"),


    /**
     * Event: 'on-load-done'
     */
    ON_LOAD_DONE("office", "load-done"),


    /**
     * Event: 'on-start-app'
     */
    ON_START_APP("office", "start-app"),


    /**
     * Event: 'on-close-app'
     */
    ON_CLOSE_APP("office", "close-app"),


    /**
     * Event: 'on-new'
     */
    ON_NEW("office", "new"),


    /**
     * Event: 'on-save'
     */
    ON_SAVE("office", "save"),


    /**
     * Event: 'on-save-as'
     */
    ON_SAVE_AS("office", "save-as"),


    /**
     * Event: 'on-print'
     */
    ON_PRINT("office", "print"),

    /**
     * Event: 'on-load-finished'
     */
    ON_LOAD_FINISHED("office", "load-finished"),


    /**
     * Event: 'on-save-finished'
     */
    ON_SAVE_FINISHED("office", "save-finished"),


    /**
     * Event: 'on-modify-changed'
     */
    ON_MODIFY_CHANGED("office", "modify-changed"),


    /**
     * Event: 'on-prepare-unload'
     */
    ON_PREPARE_UNLOAD("office", "prepare-unload"),


    /**
     * Event: 'on-new-mail'
     */
    ON_NEW_MAIL("office", "new-mail"),


    /**
     * Event: 'on-toggle-fullscreen'
     */
    ON_TOGGLE_FULLSCREEN("office", "toggle-fullscreen"),


    /**
     * Event: 'on-save-done'
     */
    ON_SAVE_DONE("office", "save-done"),


    /**
     * Event: 'on-save-as-done'
     */
    ON_SAVE_AS_DONE("office", "save-as-done"),


    /**
     * Event: 'on-create'
     */
    ON_CREATE("office", "create"),


    /**
     * Event: 'on-save-as-failed'
     */
    ON_SAVE_AS_FAILED("office", "save-as-failed"),


    /**
     * Event: 'on-save-failed'
     */
    ON_SAVE_FAILED("office", "save-failed"),


    /**
     * Event: 'on-copy-to-failed'
     */
    ON_COPY_TO_FAILED("office", "copy-to-failed"),


    /**
     * Event: 'on-title-changed'
     */
    ON_TITLE_CHANGED("office", "title-changed"),


    // Form events
    /**
     * Event: 'on-approveaction'
     */
    ON_APPROVEACTION("form", "approveaction"),


    /**
     * Event: 'on-performaction'
     */
    ON_PERFORMACTION("form", "performaction"),


    /**
     * Event: 'on-textchange'
     */
    ON_TEXTCHANGE("form", "textchange"),


    /**
     * Event: 'on-itemstatechange'
     */
    ON_ITEMSTATECHANGE("form", "itemstatechange"),


    /**
     * Event: 'on-mousedrag'
     */
    ON_MOUSEDRAG("form", "mousedrag"),


    /**
     * Event: 'on-approvereset'
     */
    ON_APPROVERESET("form", "approvereset"),


    /**
     * Event: 'on-approveupdate'
     */
    ON_APPROVEUPDATE("form", "approveupdate"),


    /**
     * Event: 'on-update'
     */
    ON_UPDATE("form", "update"),


    /**
     * Event: 'on-startreload'
     */
    ON_STARTRELOAD("form", "startreload"),


    /**
     * Event: 'on-reload'
     */
    ON_RELOAD("form", "reload"),


    /**
     * Event: 'on-startunload'
     */
    ON_STARTUNLOAD("form", "startunload"),


    /**
     * Event: 'on-confirmdelete'
     */
    ON_CONFIRMDELETE("form", "confirmdelete"),


    /**
     * Event: 'on-approverowchange'
     */
    ON_APPROVEROWCHANGE("form", "approverowchange"),


    /**
     * Event: 'on-rowchange'
     */
    ON_ROWCHANGE("form", "rowchange"),


    /**
     * Event: 'on-approvecursormove'
     */
    ON_APPROVECURSORMOVE("form", "approvecursormove"),


    /**
     * Event: 'on-cursormove'
     */
    ON_CURSORMOVE("form", "cursormove"),


    /**
     * Event: 'on-supplyparameter'
     */
    ON_SUPPLYPARAMETER("form", "supplyparameter"),


    /**
     * Event: 'on-adjust'
     */
    ON_ADJUST("form", "adjust");


    private final String namespace;
    private final String eventName;

    /**
     * @param namespace the XML ns
     * @param eventName the name
     */
    ScriptEvent(final String namespace, final String eventName) {
        this.namespace = namespace;
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public String getValue() {
        return this.namespace + ":" + this.eventName;
    }
}
