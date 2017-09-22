// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: address.proto

package com.github.liurui;

public final class AddressProtos {
  private AddressProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface AddressOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // required string city = 1;
    /**
     * <code>required string city = 1;</code>
     */
    boolean hasCity();
    /**
     * <code>required string city = 1;</code>
     */
    java.lang.String getCity();
    /**
     * <code>required string city = 1;</code>
     */
    com.google.protobuf.ByteString
        getCityBytes();

    // required string street = 2;
    /**
     * <code>required string street = 2;</code>
     */
    boolean hasStreet();
    /**
     * <code>required string street = 2;</code>
     */
    java.lang.String getStreet();
    /**
     * <code>required string street = 2;</code>
     */
    com.google.protobuf.ByteString
        getStreetBytes();
  }
  /**
   * Protobuf type {@code com.demo.Address}
   */
  public static final class Address extends
      com.google.protobuf.GeneratedMessage
      implements AddressOrBuilder {
    // Use Address.newBuilder() to construct.
    private Address(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Address(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Address defaultInstance;
    public static Address getDefaultInstance() {
      return defaultInstance;
    }

    public Address getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Address(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              city_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              street_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.github.liurui.AddressProtos.internal_static_com_demo_Address_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.github.liurui.AddressProtos.internal_static_com_demo_Address_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.github.liurui.AddressProtos.Address.class, com.github.liurui.AddressProtos.Address.Builder.class);
    }

    public static com.google.protobuf.Parser<Address> PARSER =
        new com.google.protobuf.AbstractParser<Address>() {
      public Address parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Address(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Address> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // required string city = 1;
    public static final int CITY_FIELD_NUMBER = 1;
    private java.lang.Object city_;
    /**
     * <code>required string city = 1;</code>
     */
    public boolean hasCity() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required string city = 1;</code>
     */
    public java.lang.String getCity() {
      java.lang.Object ref = city_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          city_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string city = 1;</code>
     */
    public com.google.protobuf.ByteString
        getCityBytes() {
      java.lang.Object ref = city_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        city_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    // required string street = 2;
    public static final int STREET_FIELD_NUMBER = 2;
    private java.lang.Object street_;
    /**
     * <code>required string street = 2;</code>
     */
    public boolean hasStreet() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required string street = 2;</code>
     */
    public java.lang.String getStreet() {
      java.lang.Object ref = street_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          street_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string street = 2;</code>
     */
    public com.google.protobuf.ByteString
        getStreetBytes() {
      java.lang.Object ref = street_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        street_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      city_ = "";
      street_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (!hasCity()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasStreet()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getCityBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getStreetBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getCityBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getStreetBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.github.liurui.AddressProtos.Address parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.github.liurui.AddressProtos.Address parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.github.liurui.AddressProtos.Address parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.github.liurui.AddressProtos.Address parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.github.liurui.AddressProtos.Address parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.github.liurui.AddressProtos.Address parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.github.liurui.AddressProtos.Address parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.github.liurui.AddressProtos.Address parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.github.liurui.AddressProtos.Address parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.github.liurui.AddressProtos.Address parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.github.liurui.AddressProtos.Address prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.demo.Address}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.github.liurui.AddressProtos.AddressOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.github.liurui.AddressProtos.internal_static_com_demo_Address_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.github.liurui.AddressProtos.internal_static_com_demo_Address_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.github.liurui.AddressProtos.Address.class, com.github.liurui.AddressProtos.Address.Builder.class);
      }

      // Construct using com.github.liurui.AddressProtos.Address.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        city_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        street_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.github.liurui.AddressProtos.internal_static_com_demo_Address_descriptor;
      }

      public com.github.liurui.AddressProtos.Address getDefaultInstanceForType() {
        return com.github.liurui.AddressProtos.Address.getDefaultInstance();
      }

      public com.github.liurui.AddressProtos.Address build() {
        com.github.liurui.AddressProtos.Address result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.github.liurui.AddressProtos.Address buildPartial() {
        com.github.liurui.AddressProtos.Address result = new com.github.liurui.AddressProtos.Address(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.city_ = city_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.street_ = street_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.github.liurui.AddressProtos.Address) {
          return mergeFrom((com.github.liurui.AddressProtos.Address)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.github.liurui.AddressProtos.Address other) {
        if (other == com.github.liurui.AddressProtos.Address.getDefaultInstance()) return this;
        if (other.hasCity()) {
          bitField0_ |= 0x00000001;
          city_ = other.city_;
          onChanged();
        }
        if (other.hasStreet()) {
          bitField0_ |= 0x00000002;
          street_ = other.street_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasCity()) {
          
          return false;
        }
        if (!hasStreet()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.github.liurui.AddressProtos.Address parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.github.liurui.AddressProtos.Address) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // required string city = 1;
      private java.lang.Object city_ = "";
      /**
       * <code>required string city = 1;</code>
       */
      public boolean hasCity() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required string city = 1;</code>
       */
      public java.lang.String getCity() {
        java.lang.Object ref = city_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          city_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string city = 1;</code>
       */
      public com.google.protobuf.ByteString
          getCityBytes() {
        java.lang.Object ref = city_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          city_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string city = 1;</code>
       */
      public Builder setCity(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        city_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string city = 1;</code>
       */
      public Builder clearCity() {
        bitField0_ = (bitField0_ & ~0x00000001);
        city_ = getDefaultInstance().getCity();
        onChanged();
        return this;
      }
      /**
       * <code>required string city = 1;</code>
       */
      public Builder setCityBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        city_ = value;
        onChanged();
        return this;
      }

      // required string street = 2;
      private java.lang.Object street_ = "";
      /**
       * <code>required string street = 2;</code>
       */
      public boolean hasStreet() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required string street = 2;</code>
       */
      public java.lang.String getStreet() {
        java.lang.Object ref = street_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          street_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>required string street = 2;</code>
       */
      public com.google.protobuf.ByteString
          getStreetBytes() {
        java.lang.Object ref = street_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          street_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string street = 2;</code>
       */
      public Builder setStreet(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        street_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string street = 2;</code>
       */
      public Builder clearStreet() {
        bitField0_ = (bitField0_ & ~0x00000002);
        street_ = getDefaultInstance().getStreet();
        onChanged();
        return this;
      }
      /**
       * <code>required string street = 2;</code>
       */
      public Builder setStreetBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        street_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.demo.Address)
    }

    static {
      defaultInstance = new Address(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:com.demo.Address)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_com_demo_Address_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_demo_Address_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\raddress.proto\022\010com.demo\"\'\n\007Address\022\014\n\004" +
      "city\030\001 \002(\t\022\016\n\006street\030\002 \002(\tB\"\n\021com.github" +
      ".liuruiB\rAddressProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_com_demo_Address_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_com_demo_Address_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_com_demo_Address_descriptor,
              new java.lang.String[] { "City", "Street", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}