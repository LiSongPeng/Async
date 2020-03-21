package com.light.common;

/**
 * define some constant
 */
public class Constant {
    public static final String PACKAGE_NAME_REGEX = "[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*";
    public static final String FILE_PROTOCOL = "file";
    public static final String CLASS_FILE_SUFFIX = "class";
    public static final String IPV4_REGEX = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
    public static final String IPV6_REGEX = "^(([\\da-fA-F]{1,4}):){8}$";
}
