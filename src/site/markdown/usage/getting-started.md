# Getting started with junit-quickcheck

- Create a class to host the properties you want to verify about a part of
your system. Mark it with the annotation `@RunWith(JUnitQuickcheck.class)`.
- Add `public` methods with a return type of `void` on your class, to
represent the individual properties. Mark each of them with the annotation
`@Property`.
- Run your class using JUnit. Each of your properties will be verified against
several randomly generated values for each of the parameters on the properties'
methods.

```java
    import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
    import com.pholser.junit.quickcheck.Property;

    class Crypto {
        byte[] encrypt(byte[] plaintext, Key key) {
            // ...
        }

        byte[] decrypt(byte[] ciphertext, Key key) {
            // ...
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public class SymmetricKeyCryptographyProperties {
        @Property public void decryptReversesEncrypt(String plaintext, Key key)
            throws Exception {

            Crypto crypto = new Crypto();

            byte[] ciphertext =
                crypto.encrypt(plaintext.getBytes("US-ASCII"), key);

            assertEquals(
                plaintext,
                new String(crypto.decrypt(ciphertext, key)));
        }
    }
```

*Note*: The example above presumes the existence and availability of generators
for classes `String` and `Key`. Find out more about generators in
[Basic Types](basic-types.html) and
[Generating Values of Other Types](other-types.html).

junit-quickcheck honors the usual JUnit machinery: `@Before`, `@After`,
`@Rule`, `@BeforeClass`, `@AfterClass`, `@ClassRule`. It will also run
zero-arg `public` `void` methods annotated with `@Test`.
