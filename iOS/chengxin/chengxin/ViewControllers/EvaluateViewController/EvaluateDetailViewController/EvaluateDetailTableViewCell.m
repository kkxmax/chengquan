//
//  EvaluateDetailTableViewCell.m
//  chengxin
//
//  Created by seniorcoder on 11/1/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvaluateDetailTableViewCell.h"
#import "Global.h"

@implementation EvaluateDetailTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)onZanAction:(id)sender {
    self.zanButton.selected = !self.zanButton.selected;
    if(self.zanButton.selected)
        self.electCount ++;
    else
        self.electCount --;
    self.electCountLabel.text = [NSString stringWithFormat:@"%ld", (long)self.electCount];
    NSNumber *evaluateMeID = [NSNumber numberWithInteger:self.evaluateID];
    NSNumber *evaluateMe =  self.zanButton.selected? [NSNumber numberWithInteger:1]: [NSNumber numberWithInteger:0];
    NSNumber *evaluateIndex = [NSNumber numberWithInteger:self.cellIndex];
    NSNumber *evaluateTotalValue = [NSNumber numberWithInteger:self.electCount];
    NSMutableArray *evaluateArray = [NSMutableArray array];
    [evaluateArray addObject:evaluateMeID];
    [evaluateArray addObject:evaluateMe];
    [evaluateArray addObject:evaluateIndex];
    [evaluateArray addObject:evaluateTotalValue];
    [[NSNotificationCenter defaultCenter] postNotificationName:UPDATE_EVALUATE_VIEW_NOTIFICATION object:evaluateArray];
    /*
    self.zanButton.selected = !self.zanButton.selected;
    if(self.zanButton.selected)
        self.electCount ++;
    else
        self.electCount --;
    self.electCountLabel.text = [NSString stringWithFormat:@"%ld", (long)self.electCount];
    NSNumber *evaluateMeID = [NSNumber numberWithInteger:self.evaluateID];
    NSNumber *evaluateMe =  self.zanButton.selected? [NSNumber numberWithInteger:1]: [NSNumber numberWithInteger:0];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"elect" forKey:@"pAct"];
    [dicParams setObject:evaluateMeID forKey:@"estimateId"];
    [dicParams setObject:evaluateMe forKey:@"val"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_UPDATE_EVALUATE_ME Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
            }
        }
    }];
     */
}

- (IBAction)onErrorAction:(id)sender {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        [GeneralUtil showRealnameAuthAlertWithDelegate:self];
    } else {
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_EVALUATE_FIXED_ERROR_VIEW_NOTIFICATION object:nil];
    }
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
        [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_REALNAMEAUTH_VIEW_NOTIFICATION object:nil];
}



@end
