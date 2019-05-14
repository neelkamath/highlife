# Highlife

This is an implementation of the <a href="https://en.wikipedia.org/wiki/Highlife_(cellular_automaton)">Highlife</a> cellular automaton.

## Installation

1. Install a version of Kotlin not less than 1.3, and less than 2 from [here](https://kotlinlang.org/docs/tutorials/command-line.html).
1. Clone the repository using one of the following methods.
    - SSH: `git clone git@github.com:neelkamath/highlife.git`
    - HTTPS: `git clone https://github.com/neelkamath/highlife.git`

## Usage

`<GRADLE> run -q --console=plain`, where `<GRADLE>` is `gradle.bat` on Windows, and `./gradlew` on others.

`1` and `0` denote alive and dead cells respectively.

## Testing

- Windows: `gradle.bat test`
- Other: `./gradlew test`

## License

This project is under the [MIT License](LICENSE).