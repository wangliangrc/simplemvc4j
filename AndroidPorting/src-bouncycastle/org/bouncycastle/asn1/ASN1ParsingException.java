package org.bouncycastle.asn1;

public class ASN1ParsingException extends IllegalStateException {
    private static final long serialVersionUID = -5211721538578365706L;
    private Throwable cause;

    ASN1ParsingException(String message) {
        super(message);
    }

    ASN1ParsingException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
