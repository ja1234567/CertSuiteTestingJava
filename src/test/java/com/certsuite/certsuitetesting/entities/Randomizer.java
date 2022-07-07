package com.certsuite.certsuitetesting.entities;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.WordUtils;

import java.util.Random;

public class Randomizer {

    static Random rand = new Random();

    static char[] vowels = new char[] {
            'a',
            'e',
            'i',
            'o',
            'u'};

    static char[] consonants = new char[] {
            'b',
            'c',
            'd',
            'f',
            'g',
            'h',
            'j',
            'k',
            'l',
            'm',
            'n',
            'p',
            'q',
            'r',
            's',
            't',
            'v',
            'w',
            'x',
            'y',
            'z'};

    static String[] maleNames = new String[] {
            "James",
            "Robert",
            "John",
            "Michael",
            "William",
            "David",
            "Richard",
            "Joseph",
            "Thomas",
            "Charles",
            "Christopher",
            "Daniel",
            "Matthew",
            "Anthony",
            "Mark",
            "Donald",
            "Steven",
            "Paul",
            "Andrew",
            "Joshua",
            "Kenneth",
            "Kevin",
            "Brian",
            "George",
            "Edward",
            "Ronald",
            "Timothy",
            "Jason",
            "Jeffrey",
            "Ryan",
            "Jacob",
            "Gary",
            "Nicholas",
            "Eric",
            "Jonathan",
            "Stephen",
            "Larry",
            "Justin",
            "Scott",
            "Brandon",
            "Brandon",
            "Samuel",
            "Gregory",
            "Frank",
            "Alexander",
            "Raymond",
            "Patrick",
            "Jack",
            "Dennis",
            "Jerry"};

    static String[] femaleNames = new String[] {
            "Mary",
            "Patricia",
            "Jennifer",
            "Linda",
            "Elizabeth",
            "Barbara",
            "Susan",
            "Jessica",
            "Sarah",
            "Karen",
            "Nancy",
            "Lisa",
            "Betty",
            "Margaret",
            "Sandra",
            "Ashley",
            "Kimberly",
            "Emily",
            "Donna",
            "Michelle",
            "Dorothy",
            "Carol",
            "Amanda",
            "Melissa",
            "Deborah",
            "Stephanie",
            "Rebecca",
            "Sharon",
            "Cynthia",
            "Kathleen",
            "Amy",
            "Shirley",
            "Angela",
            "Helen",
            "Anna",
            "Brenda",
            "Pamela",
            "Nicole",
            "Emma",
            "Samantha",
            "Katherine",
            "Christine",
            "Debra",
            "Rachel",
            "Catherine",
            "Carolyn",
            "Janet",
            "Ruth",
            "Maria"};

    static String[] domains = new String[] {
            "outlook",
            "gmail",
            "hotmail",
            "yahoo",
            "microsoft",
            "myspace",
            "virgin",
            "bt",
            "talktalk",
            "o2",
            "giffgaff",
            "facebook",
            "instagram",
            "twitter",
            "megger",
            "evil"};

    static String[] suffixes = new String[] {
            "com",
            "gov",
            "biz",
            "co.uk",
            "eu",
            "us",
            "au",
            "africa",
            "vn",
            "mx",
            "ie",
            "kr",
            "jp",
            "lv",
            "io",
            "ly",
            "biz",
            "gov",
            "it",
            "central"};

    static String[] prefixes = new String[] {
            "44",
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "00",
            "0300",
            "01491",
            "020",
            "0800",
            "1800",
            "141",
            "1800",
            "989"};

    static String[] seperators = new String[] {
            " ",
            " and ",
            " & ",
            ", sons of ",
            " in partnership with ",
            " with "};

    static String[] endings = new String[] {
            " inc.",
            " ltd.",
            " PLC.",
            " and Sons",
            " Partnership",
            " Limited",
            " Store",
            " Station",
            " Express",
            "-Care",
            " Center",
            " Charity",
            " at Heart",
            " Global",
            " National",
            " Pharma",
            " X",
            " Assoc.",
            " Industries",
            " Corp."};

    static String[] merch = new String[] {
            " Perfume",
            " Software",
            " Deliveries",
            " Toothpaste",
            " Batteries",
            " Clocks",
            " Phones",
            " Animation",
            " Water",
            " Vitamins",
            " Vaccines",
            " Electronics",
            " Fried Chicken",
            " Recycling",
            " Solar",
            " Comics"};

    static String[] addressSuffixes = new String[] {
            "Lane",
            "Road",
            "Park",
            "Street",
            "Gate",
            "Close",
            "Cresent",
            "Broadway",
            "Way",
            "Walk",
            "Hill",
            "Drive",
            "Terrace",
            "Place",
            "Mews",
            "Gardens",
            "Fields"};

    static String[] townPrefixes = new String[] {
            "Ox",
            "Abing",
            "Ban",
            "Carter",
            "Wit",
            "Bi",
            "Did",
            "Want",
            "Cat",
            "Peck",
            "Brom",
            "Lewis"};

    static String[] townSuffixes = new String[] {
            "ford",
            "don",
            "bury",
            "ton",
            "ney",
            "cester",
            "cot",
            "tage",
            "ley",
            "ham"};

    public static String RandomName() {
        if ((rand.nextInt(11) > 5)) {
            return maleNames[rand.nextInt(maleNames.length)];
        }

        return femaleNames[rand.nextInt(femaleNames.length)];
    }

    public static String RandomFullName() {
        return (Randomizer.RandomName() + (" " + Randomizer.RandomName()));
    }

    public static String RandomMaleName() {
        return maleNames[rand.nextInt(maleNames.length)];
    }

    public static String RandomFemaleName() {
        return femaleNames[rand.nextInt(femaleNames.length)];
    }

    public static String RandomEmail(String fn, String ln) {
        if ((fn == null)) {
            fn = Randomizer.RandomName();
        }

        if ((ln == null)) {
            ln = Randomizer.RandomName();
        }

        var middle = ".";
        if ((rand.nextInt(11) > 5)) {
            middle = Randomizer.RandomName();
        }
        else if ((rand.nextInt(11) > 8)) {
            fn = fn.substring(0, 1);
        }

        var x = rand.nextInt(10000);
        var d = rand.nextInt(domains.length);
        var s = rand.nextInt(suffixes.length);
        return (fn
                + (middle
                + (ln
                + (x + ("@"
                + (domains[d] + ("." + suffixes[s])))))));
    }

    public static String RandomPhoneNumber() {
        StringBuilder restOfNumber = new StringBuilder();
        for (var i = 0; (i < 9); i++) {
            restOfNumber.append(rand.nextInt(10));
        }

        return ((prefixes[rand.nextInt(prefixes.length)] + restOfNumber)).substring(0, 11);
    }

    public static String RandomAddress() {
        return (rand.nextInt(300) + (" "
                + (Randomizer.RandomName() + (" " + addressSuffixes[rand.nextInt(addressSuffixes.length)]))));
    }

    public static String RandomTown() {
        return (townPrefixes[rand.nextInt(townPrefixes.length)] + townSuffixes[rand.nextInt(townSuffixes.length)]);
    }

    public static String RandomPostcode() {
        char[] letters = ArrayUtils.addAll(vowels, consonants);
        return "" + letters[rand.nextInt(letters.length)]
                + letters[rand.nextInt(letters.length)]
                + rand.nextInt(100)
                + letters[rand.nextInt(letters.length)]
                + letters[rand.nextInt(letters.length)]
                + rand.nextInt(100);
    }

    public static String RandomCompanyName() {
        var companyName = "";
        var numberOfNames = (rand.nextInt(2) + 1);
        int x = rand.nextInt(11);
        if ((x > 4)) {
            companyName = (companyName + Randomizer.RandomAppName());
        }
        else if ((x > 3)) {
            companyName = (companyName + Randomizer.RandomInitials());
        }
        else {
            for (var i = 0; (i < numberOfNames); i++) {
                companyName = Randomizer.RandomName();
                if ((i
                        < (numberOfNames - 1))) {
                    companyName = (companyName + seperators[rand.nextInt(seperators.length)]);
                }
            }
        }

        var ending = rand.nextInt(11);
        if (((numberOfNames == 1)
                || (ending > 4))) {
            if ((ending < 8)) {
                companyName = (companyName + endings[rand.nextInt(endings.length)]);
            }
            else if ((ending < 9)) {
                companyName = (companyName + ("\'s" + merch[rand.nextInt(merch.length)]));
            }
            companyName = (companyName + merch[rand.nextInt(merch.length)]);
        }
        return companyName;
    }

    public static String RandomString(int length) {
        StringBuilder sb = new StringBuilder("");
        char[] characters = ArrayUtils.addAll(vowels,consonants);
        characters = ArrayUtils.addAll(characters,new char[' ']);
        for (int i = 0; (i < length); i++) {
            sb.append(characters[rand.nextInt(27)]);
        }
        return sb.toString();
    }

    private static String RandomAppName() {
        StringBuilder appName = new StringBuilder();
        var noOfLetters = (rand.nextInt(3) + 3);
        for (var i = 0; (i < noOfLetters); i++) {
            if (i % 2 == 0) {
                var letter = consonants[rand.nextInt(consonants.length)];
                appName.append(letter);
            }
            else {
                appName.append(vowels[rand.nextInt(vowels.length)]);
            }
        }

        return WordUtils.capitalizeFully(appName.toString(), new char[' ']);
    }

    private static String RandomInitials() {
        int x = (rand.nextInt(2) + 3);
        return Randomizer.RandomString(x).toUpperCase();
    }
}