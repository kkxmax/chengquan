//
//  HotDetailPersonalCell.m
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HotDetailPersonalCell.h"
#import "Global.h"

@implementation HotDetailPersonalCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)onClickMoreReplyButton:(id)sender {
    self.moreReplyButton.selected = !self.moreReplyButton.selected;
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOT_MORE_REPLY_VIEW_NOTIFICATION object:nil];
}
- (IBAction)onZanAction:(id)sender {
    self.zanButton.selected = !self.zanButton.selected;
    if(self.zanButton.selected)
        self.electCount ++;
    else
        self.electCount --;
    self.lblElect.text = [NSString stringWithFormat:@"%ld", (long)self.electCount];
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
    self.lblElect.text = [NSString stringWithFormat:@"%ld", (long)self.electCount];
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
@end
