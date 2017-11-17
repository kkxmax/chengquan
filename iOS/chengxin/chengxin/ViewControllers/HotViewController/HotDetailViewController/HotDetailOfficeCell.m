//
//  HotDetailOfficeCell.m
//  chengxin
//
//  Created by seniorcoder on 11/9/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotDetailOfficeCell.h"
#import "Global.h"

@implementation HotDetailOfficeCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)onClickFavouriteButton:(id)sender {
    [self setInterested];
}

- (void)setInterested {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"setInterest" forKey:@"pAct"];
    [dicParams setObject:self.accountID forKey:@"accountId"];
    if(self.btnFavourite.isSelected) {
        [dicParams setObject:@"0" forKey:@"val"];
    }else{
        [dicParams setObject:@"1" forKey:@"val"];
    }
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_SETINTEREST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                if(self.btnFavourite.isSelected) {
                    self.btnFavourite.selected = NO;
                }else{
                    self.btnFavourite.selected = YES;
                }
            }else{
                
            }
        }
    }];
    
}
@end
