// characters that won't be used on the carrier
def notUsableChars = [' ', ',', '.', '-', '_', 'ñ','\n','\r','\t','?','!']

// pairs for the code
def pairs = [['a','AAAAA'], ['g','AABBA'], ['n','ABBAA'], ['t','BAABA'],
['b','AAAAB'], ['h','AABBB'], ['o','ABBAB'], ['u','BAABB'],
['v','BAABB'], ['c','AAABA'], ['i','ABAAA'], ['j','ABAAA'],
['p','ABBBA'], ['w','BABAA'], ['d','AAABB'], ['k','ABAAB'],
['q','ABBBB'], ['x','BABAB'], ['e','AABAA'], ['l','ABABA'],
['r','BAAAA'], ['y','BABBA'], ['f','AABAB'], ['m','ABABB'],
['s','BAAAB'], ['z','BABBB']]

// prepare encoding map for easyness of use
def encode = [:]
pairs.each{
    encode[it[0]] = it[1]
}

//prepare decoding map for easyness of use
def decode = [:]
pairs.each{
    if(decode.containsKey(it[1])){
        decode[it[1]] += "/${it[0]}"
    }else{
        decode[it[1]] = it[0]
    }
}

// returns string equal to the passed one but without not usable chars
def cleanString = {
	String tmp = new String(it)
	notUsableChars.each{
		tmp = tmp.replace it, ''
	}
	return tmp
}

// enters the message on the carrier coding it using BACON coding
def bacon = { carrier, message ->
	String codedMessage = ''
	// assert message enters on the carrier
	assert cleanString(carrier).size() >= (cleanString(message).size() * 5)
	// var for next usable carrier character
	int pos = 0
	// iterate through each messaje character
	message.each{
		// get code word for the character
		String codeWord = encode[it.toLowerCase()]
		// iterate codeWord characters
		codeWord.each{
			// get next carrier character
			String next = carrier[pos++]
			// skip not usable characters
			while(next in notUsableChars){
				codedMessage += next
				// and get the next character
				next = carrier[pos++]
			}
			if(it == 'A'){
				// append it as lower case when A
				codedMessage += next.toLowerCase()
			}else{
				// append it as upper case when B
				codedMessage += next.toUpperCase()
			}
		}
	}
	// append the rest of the text if any
	while(pos < carrier.size()){
		codedMessage += carrier[pos++]
	}
	// return the coded message
	return codedMessage
}

// decodes the hidden message on the passed message using BACON code
def deBacon = { text ->
	String codedMessage = ''
	String part = ''
	// iterate every fith position on the text length
	text.each{
		// use only usable characters
		if(!(it in notUsableChars)){
			if(Character.isLowerCase(it as char)){
				// is it's lower case -> 'A'
				part += 'A'
			}else{
				// is it's upper case -> 'A'
				part += 'B'
			}
			// when the part is 5 characters long
			if(part.size() == 5){
				// find decoding character/s
				codedMessage +=decode[part]
				// restart the part
				part = ''
			}
		}
	}
	return codedMessage
}

// small example
println bacon('This is the text that will work as carrier for the hidden message.','groovy')
// will produce extra a characters as length of hidden message is not specified
println deBacon('thIS iS the teXT tHaT WiLL woRK As CArrier for the hidden message.')
// extra long example
println bacon('''Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
Phasellus varius lectus vitae accumsan luctus. Vestibulum massa est, tempor 
ac commodo malesuada, gravida rutrum augue.''','This is a little bigger example')
