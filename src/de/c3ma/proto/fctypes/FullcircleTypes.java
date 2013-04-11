package de.c3ma.proto.fctypes;

/**
 * created at 11.04.2013 - 18:13:12<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * 
 * @author ollo<br />
 */
public interface FullcircleTypes {

    // Defines based on sequence.proto from Fullcircle
    // BinarySequenceMetadata
    int BINARYSEQUENCEMETADATA_FRAMESPERSECOND = 1;
    int BINARYSEQUENCEMETADATA_WIDTH = 2;
    int BINARYSEQUENCEMETADATA_HEIGHT = 3;
    int BINARYSEQUENCEMETADATA_GENERATORNAME = 4;
    int BINARYSEQUENCEMETADATA_GENERATORVERSION = 5;

    // RGBValue
    int RGBVALUE_RED = 1;
    int RGBVALUE_GREEN = 2;
    int RGBVALUE_BLUE = 3;
    int RGBVALUE_X = 4;
    int RGBVALUE_Y = 5;

    // BinaryFrame
    int BINARYFRAME_PIXEL = 1;

    // BinarySequence
    int BINARYSEQUENCE_METADATA = 1;
    int BINARYSEQUENCE_FRAME = 2;

    // Network Snip
    int SNIP_TYPE = 1;

    int SNIPTYPE_PING = 1;
    int SNIPTYPE_PONG = 2;
    int SNIPTYPE_ERROR = 3;
    int SNIPTYPE_REQUEST = 4;
    int SNIPTYPE_START = 5;
    int SNIPTYPE_FRAME = 6;
    int SNIPTYPE_ACK = 7;
    int SNIPTYPE_NACK = 8;
    int SNIPTYPE_TIMEOUT = 9;
    int SNIPTYPE_ABORT = 10;
    int SNIPTYPE_EOS = 11;
    int SNIPTYPE_INFOREQUEST = 12;
    int SNIPTYPE_INFOANSWER = 13;

    int SNIP_PINGSNIP = 11;
    int PINGSNIP_COUNT = 1;

    int SNIP_PONGSNIP = 12;
    int PONGSNIP_COUNT = 1;

    int ERRORCODETYPE_OK = 1;
    int ERRORCODETYPE_DECODING_ERROR = 2;

    int SNIP_ERRORSNIP = 13;
    int ERRORSNIP_ERRORCODE = 1;
    int ERRORSNIP_DESCRIPTION = 2;

    int SNIP_REQUESTSNIP = 14;
    int REQUESTSNIP_COLOR = 1;
    int REQUESTSNIP_SEQID = 2;
    int REQUESTSNIP_META = 3;

    int SNIP_STARTSNIP = 15;

    int SNIP_FRAMESNIP = 16;
    int FRAMESNIP_FRAME = 1;

    int SNIP_ACKSNIP = 17;

    int SNIP_NACKSNIP = 18;

    int SNIP_TIMEOUTSNIP = 19;

    int SNIP_ABORTSNIP = 20;

    int SNIP_EOSSNIP = 21;

    int SNIP_INFOREQUEST = 22;

    int SNIP_INFOANSWERSNIP = 23;
    int INFOANSWERSNIP_META = 1;

}
