import fileinput
from nltk.corpus import wordnet as wn

COUNT = 0;
WORDS = {}
POS = {
    'v': 'verb', 'a': 'adjective', 's': 'satellite adjective', 
    'n': 'noun', 'r': 'adverb'}

def crawl(word, level=1):
  if ( level == 0):
    return None;

  related = None;

  try:
    related = info(word);
  except:
    #return None;
    raise

  if ( related is not None):
    for (i,r) in enumerate(related):
       crawl(r, (level - 1));

def info(word, pos=None):
  global COUNT;
  WORDS[word] = 1;
  synonyms = list();
  antonyms = list();
  hypernyms = list();
  hyponyms = list();
  related = list();
  meanings = list();
  for i, syn in enumerate(wn.synsets(word, pos)):
     syns = [n.replace('_', ' ') for n in syn.lemma_names]
     ants = [a.name.replace(';',' ') for m in syn.lemmas for a in m.antonyms()]
     ants = [a.name.replace(';',' ') for m in syn.lemmas for a in m.antonyms()]
     hypos = [a.name.replace(';',' ') for m in syn.lemmas for a in m.hyponyms()]
     hypers = [a.name.replace(';',' ') for m in syn.lemmas for a in m.hypernyms()]

     for (j,s) in enumerate(syns):
       s = s.lower();
       if ( s != word): 
         synonyms.append(s);
         if ( s not in WORDS):
           related.append(s);
     for (j,a) in enumerate(ants):
       a = a.lower();
       if ( a != word): 
         antonyms.append(a);
         if ( a not in WORDS):
           related.append(a);
     for (j,a) in enumerate(hypos):
       a = a.lower();
       if ( a != word): 
         hyponyms.append(a);
         if ( a not in WORDS):
           related.append(a);
     for (j,a) in enumerate(hypers):
       a = a.lower();
       if ( a != word): 
         hypernyms.append(a);
         if ( a not in WORDS):
           related.append(a);
     meanings.append(syn.definition.replace(';',' '))

  if not meanings:
     return None;

  print "%s;%s;%s;%s;%s;%s" % (word, str(meanings), str(synonyms), str(antonyms),str(hypernyms),str(hyponyms));
  COUNT += 1;

  if ( COUNT > 1000000):
    sys.exit(0);

  return related;
 
for line in fileinput.input():
  word = line.rstrip().lower();
  crawl(word,3)
